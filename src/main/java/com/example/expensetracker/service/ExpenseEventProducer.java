package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.Expense;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ExpenseEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemp;

    public ExpenseEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemp = kafkaTemplate;
    }

    public void publishExpenseEventCreatedEvent(Expense expense) {
        kafkaTemp.send("expense-events", expense.getId().toString(), expense);
    }

    public void publishExpenseDeletedEvent(UUID id) {
        ExpenseDeletedEvent expenseDeletedEvent = new ExpenseDeletedEvent(id);
        kafkaTemp.send("expense-deleted-events", id.toString(), expenseDeletedEvent);
    }
}
