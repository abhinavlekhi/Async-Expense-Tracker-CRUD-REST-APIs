package com.example.expensetracker.scheduler;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ExpenseSummaryScheduler {
    private static final Logger logger= LoggerFactory.getLogger(ExpenseSummaryScheduler.class);
    // Injected ExpenseRepository in order to fetch expenses from the DB
    private final ExpenseRepository expenseRepository;

    // This constructor is used by spring to inject ExpenseRepository dependency, but why ExpenseRepository not
    // expenseService?? because here we need the db to fetch expenses from past day.
    public ExpenseSummaryScheduler(ExpenseRepository expenserepository) {
        this.expenseRepository = expenserepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailySummary() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<Expense> expenses = expenseRepository.findByDateBetween(yesterday, today);

        double total = 0.0;
        for (Expense expens : expenses) {
            BigDecimal amount = expens.getAmount();
            total = total + amount.doubleValue();
        }
        logger.info(" Daily expense summary (last 24 hrs) "+total);
    }
}
