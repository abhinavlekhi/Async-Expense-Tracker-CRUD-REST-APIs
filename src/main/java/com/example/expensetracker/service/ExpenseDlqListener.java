package com.example.expensetracker.service;

import com.example.expensetracker.events.ExpenseDeletedEvent;
import com.example.expensetracker.model.DlqEvent;
import com.example.expensetracker.repository.DlqEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class ExpenseDlqListener {

    @Autowired
    private final DlqEventRepository dlqEventRepository;

    @Autowired
    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(ExpenseDlqListener.class);

    public ExpenseDlqListener(DlqEventRepository dlqEventRepository, ObjectMapper objectMapper) {
        this.dlqEventRepository = dlqEventRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics="expense-deleted-events-dlt",
            groupId = "dlq-consumer-group",
            containerFactory = "dlqKafkaListenerContainerFactory")
    public void handleDlq(ExpenseDeletedEvent ede, @Header(name = "kafka_exception-message", required = false) String errorMessage) throws
            JsonProcessingException {
        DlqEvent dlq = new DlqEvent();
        dlq.setEventType("ExpenseDeletedEvent");
        dlq.setPayload(objectMapper.writeValueAsString(ede));
        dlq.setErrorMessage(errorMessage);
        try {
            dlqEventRepository.save(dlq);
        } catch(Exception e) {
            log.error("DLQ Event received: {}, Exception: {}", dlq, e.getMessage());
        }
        System.out.println("DLQ Event stored in DB: "+ ede.getId());
    }
}
