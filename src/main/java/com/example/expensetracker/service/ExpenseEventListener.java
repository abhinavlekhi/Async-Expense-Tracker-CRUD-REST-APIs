package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventListener {
    @Autowired // spring automatically injects the Repository dependency here
    private ExpenseRepository expenseRepository;

    @KafkaListener(topics = "expense-events", groupId = "expense-consumer-group")
    public void handleExpenseEventCreatedEvent(Expense expense) {
        System.out.println("Received Expense event: "+ expense);
    }

    @KafkaListener(topics = "expense-deleted-events", groupId = "expense-consumer-group")
    public void handleExpenseDeletedEvent(ExpenseDeletedEvent expenseDeletedEvent) {
        if (System.currentTimeMillis() % 2 == 0) {   // condition added to simulate failures and observe retries + DLQ behavior
            throw new RuntimeException("Simulated downstream processing failed for id: "+expenseDeletedEvent.getId());
        }
        System.out.println("Received Expense deleted event: "+ expenseDeletedEvent.getId());
    }
}
