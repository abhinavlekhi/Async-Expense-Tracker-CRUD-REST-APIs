package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemp;

    public ExpenseEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemp = kafkaTemplate;
    }

    public void publishExpenseEventCreatedEvent(Expense expense) {
        kafkaTemp.send("expense-events", expense.getId().toString(), expense);
    }
}
