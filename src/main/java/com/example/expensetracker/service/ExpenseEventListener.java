package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventListener {
    @Autowired // spring automatically injects the Repository dependency here
    private ExpenseRepository expenseRepository;

    @KafkaListener(topics = "expense-events", groupId = "expense-consumer-group", concurrency = "3") //concurrency = "3" tells Spring Kafka to run three consumer workers for this listener. Now If expense-events has three partitions, up to three workers can process events in parallel—one per partition, For example, while worker 1 handles an event from partition 0, workers 2 and 3 can handle events from partitions 1 and 2. Events in the same partition remain ordered, but events from different partitions may be processed concurrently.
    public void handleExpenseEventCreatedEvent(Expense expense,
                                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        System.out.println("Received Expense event from partition "+ partition+ ": "+ expense);
    }

    @KafkaListener(topics = "expense-deleted-events", groupId = "expense-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleExpenseDeletedEvent(ExpenseDeletedEvent expenseDeletedEvent) {
        if (System.currentTimeMillis() % 2 == 0) {   // condition added to simulate failures and observe retries + DLQ behavior
            throw new RuntimeException("Simulated downstream processing failed for id: "+expenseDeletedEvent.getId());
        }
        System.out.println("Received Expense deleted event: "+ expenseDeletedEvent.getId());
    }

//    @KafkaListener(topics = "expense-events", groupId = "expense-consumer-group", containerFactory = "kafkaListenerContainerFactory")
//    public void handleExpenseUpdatedEvent(Expense expense,
//                                          @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//        System.out.println("Received Expense updated event from partition " + partition + ": " + expense);
//    }
}
