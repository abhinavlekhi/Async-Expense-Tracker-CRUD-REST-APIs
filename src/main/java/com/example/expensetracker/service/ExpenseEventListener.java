package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.Expense;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventListener {

    @KafkaListener(topics = "expense-events", groupId = "expense-consumer-group")
    public void handleExpenseEventCreatedEvent(Expense expense) {
        System.out.println("Received Expense event: "+ expense);
    }

    @KafkaListener(topics = "expense-deleted-events", groupId = "expense-consumer-group")
    public void handleExpenseDeletedEvent(ExpenseDeletedEvent expenseDeletedEvent) {
        System.out.println("Received Expense deleted event: "+ expenseDeletedEvent);
    }
}
