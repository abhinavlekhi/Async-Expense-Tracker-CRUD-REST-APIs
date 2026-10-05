package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.ProcessedEvent;
import com.example.expensetracker.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpenseEventProcessingService {

    private final ProcessedEventRepository processedEventRepository;

    public ExpenseEventProcessingService(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository=processedEventRepository;
    }

    @Transactional
    public boolean recordIfFirstDelivery(ExpenseDeletedEvent expenseDeletedEvent) {
        if(expenseDeletedEvent.getEventId() == null) {
            throw new IllegalArgumentException("Delete Event Must Have An eventId");
        }

        if(processedEventRepository.existsById(expenseDeletedEvent.getEventId())) {
            return false;
        }

        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventId(expenseDeletedEvent.getEventId());
        processedEvent.setEventType("ExpenseDeletedEvent");
        processedEvent.setProcessedAt(LocalDateTime.now());
        processedEventRepository.save(processedEvent);
        return true;
    }
}
