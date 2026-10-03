package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequestDTO;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private ExpenseEventProducer producer;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void addExpenseSavesPublishesAndAuditsExpense() {
        ExpenseRequestDTO dto = request("Groceries", "42.50", "Weekly shop",
                LocalDate.of(2026, 9, 1));
        Expense saved = expense(UUID.randomUUID(), "Groceries", "42.50", "Weekly shop",
                LocalDate.of(2026, 9, 1));
        when(expenseRepository.save(any(Expense.class))).thenReturn(saved);

        Expense result = expenseService.addExpense(dto);

        assertSame(saved, result);
        ArgumentCaptor<Expense> captor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(captor.capture());
        assertEquals("Groceries", captor.getValue().getExpenseTitle());
        assertEquals(new BigDecimal("42.50"), captor.getValue().getAmount());
        assertEquals("Weekly shop", captor.getValue().getNotes());
        assertEquals(LocalDate.of(2026, 9, 1), captor.getValue().getDate());
        verify(producer).publishExpenseEventCreatedEvent(saved);
        verify(auditService).logAudit("CREATED", saved);
    }

    @Test
    void getAllExpenseUsesTitleAndDateWhenBothAreProvided() {
        LocalDate date= LocalDate.of(2026, 9, 1);
        List<Expense> expected = List.of(expense(UUID.randomUUID(), "Groceries", "42.50", null, date));
        when(expenseRepository.findByExpenseTitleAndDate("Groceries", date)).thenReturn(expected);
        assertSame(expected, expenseService.getAllExpense("Groceries", date));
        verify(expenseRepository).findByExpenseTitleAndDate("Groceries", date);
    }

    @Test
    void getAllExpenseUsesTitleWhenOnlyTitleIsProvided() {
        List<Expense> expected = List.of();
        when(expenseRepository.findByExpenseTitle("Groceries")).thenReturn(expected);

        assertSame(expected, expenseService.getAllExpense("Groceries", null));
        verify(expenseRepository).findByExpenseTitle("Groceries");
    }

    @Test
    void getAllExpenseUsesDateWhenOnlyDateIsProvided() {
        LocalDate date= LocalDate.of(2026, 9, 1);
        List<Expense> expected = List.of();
        when(expenseRepository.findByDate(date)).thenReturn(expected);

        assertSame(expected, expenseService.getAllExpense(null, date));
        verify(expenseRepository).findByDate(date);
    }

    @Test
    void getAllExpenseReturnsAllExpensesWhenNoFiltersAreProvided() {
        List<Expense> expected = List.of();
        when(expenseRepository.findAll()).thenReturn(expected);
        assertSame(expected, expenseService.getAllExpense(null, null));
        verify(expenseRepository).findAll();
    }

    private static ExpenseRequestDTO request(
            String title, String amount, String notes, LocalDate date) {
        ExpenseRequestDTO dto = new ExpenseRequestDTO();
        dto.setExpenseTitle(title);
        if (amount != null) {
            dto.setAmount(new BigDecimal(amount));
        }
        dto.setNotes(notes);
        dto.setDate(date);
        return dto;
    }

    private static Expense expense(
            UUID id, String title, String amount, String notes, LocalDate date) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setExpenseTitle(title);
        expense.setAmount(new BigDecimal(amount));
        expense.setNotes(notes);
        expense.setDate(date);
        return expense;
    }
}

