package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequestDTO;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.specification.ExpenseSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service //service layer is where business logic is kept (this is managed as Spring Bean), it acts a bridge controller (handles HTTP requests) and the repository (handles DB operations)
public class ExpenseService {

    @Autowired // spring automatically injects the Repository dependency here
    private ExpenseRepository expenseRepository;
    @Autowired
    private AuditService auditService;
    @Autowired
    private ExpenseEventProducer producer;

    // 1. Create  (Add new Expense)
    @CacheEvict(value= "expenseCache", /*,"all_expenses"*/ allEntries = true)
    public Expense addExpense(ExpenseRequestDTO dto) {

        Expense expense = new Expense();
        expense.setExpenseTitle(dto.getExpenseTitle());
        expense.setAmount(dto.getAmount());
        expense.setNotes(dto.getNotes());
        expense.setDate(dto.getDate());
        Expense saved = expenseRepository.save(expense);
        auditService.logAudit("CREATED", saved);
        producer.publishExpenseEventCreatedEvent(saved);
        return saved;
    }

    // 2. Read (Get all expenses)
    public List<Expense> getAllExpense(String title, LocalDate date) {
        if (title != null && date != null) { // if both the query params are passed
            return expenseRepository.findByExpenseTitleAndDate(title, date);
        }
        if (title != null) { // if only query param title is passed
            return expenseRepository.findByExpenseTitle(title);
        }
        if (date != null) { // if only query param date is passed
            return expenseRepository.findByDate(date);
        }
        return expenseRepository.findAll();
    }

    // 3. Read (find expense by id)
    @Cacheable(value= "expenseCache", key= "#id")
    public Expense getExpenseById(UUID id) {
        return expenseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Expense with id "+id+" not found"));
    }

    // 4. Delete (delete expense by id)
    @CacheEvict(value = {"expenseCache"/*,"all_expenses"*/}, key= "#id", allEntries = true)
    public void deleteExpenseById(UUID id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Could not delete the Expense as no expense was found with provided id: "+id));
        producer.publishExpenseDeletedEvent(id);
        auditService.logAudit("DELETED", expense);
        expenseRepository.deleteById(id);
    }

    // 5. Update (expense by id)
    @CacheEvict(value = {"expenseCache" /*,"all_expenses"*/}, key= "#id", allEntries = true)
    public Expense updateExpensePartially(UUID id, ExpenseRequestDTO dto) {
        Expense existingExpense = expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not update the Expense as no expense was found with provided id: " + id));
        if (dto.getExpenseTitle() != null && !dto.getExpenseTitle().equals(existingExpense.getExpenseTitle())) {
            if (expenseRepository.existsByExpenseTitle(dto.getExpenseTitle())) {
                throw new IllegalArgumentException("Title must be unique");
            }
            existingExpense.setExpenseTitle(dto.getExpenseTitle());
        }

        if (dto.getAmount() != null) {
            existingExpense.setAmount(dto.getAmount());
        }

        if (dto.getNotes() != null) {
            existingExpense.setNotes(dto.getNotes());
        }

        if(dto.getDate() != null) {
            existingExpense.setDate(dto.getDate());
        }
        auditService.logAudit("UPDATED", expenseRepository.save(existingExpense));
        return expenseRepository.save(existingExpense);
    }

    public Page<Expense> getExpenses(Pageable pageable) {
        return expenseRepository.findAll( pageable);
    }

    //@Cacheable(value= "all_expenses")
    public Page<Expense>getFilteredExpenses (String title, LocalDate date, Pageable pageable) {
        Specification<Expense> spec = (root, query, cb) -> cb.conjunction();
        if (title != null && !title.isEmpty()) {
            spec = spec.and(ExpenseSpecification.hasTitle(title));
        }
        if (date != null) {
            spec = spec.and(ExpenseSpecification.hasDate(date));
        }
        return expenseRepository.findAll(spec, pageable);
    }
}
