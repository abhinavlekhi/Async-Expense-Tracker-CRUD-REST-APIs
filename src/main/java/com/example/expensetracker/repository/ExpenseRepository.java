package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// here we created an ExpenseRepository interface because spring boot will generate the actual implementation for us
// and reason of extending JpaRepository interface is it provides ready to use methods such as findAll(){ get all records}, findById(id) {to fetch one record}, etc...
@Repository //This annotation marks the class as Data Access layer component and spring uses it for Exception translation (SQL -> Spring Exceptions)
public interface ExpenseRepository extends JpaRepository<Expense, UUID>, JpaSpecificationExecutor<Expense> {

    List<Expense> findByExpenseTitle(String title);
    List<Expense> findByDate(LocalDate date);
    List<Expense> findByExpenseTitleAndDate(String title, LocalDate date);
    boolean existsByExpenseTitle(String title);
    //findByDateBetween will be derived by spring, but how?? because of it's naming convention,
    // but if we give a dummy name like ABCD?? then it won't be able to derive it and will throw error at run time,
    // so always follow naming conventions for spring jpa derived queries
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
