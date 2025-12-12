package com.example.expensetracker.service;

import com.example.expensetracker.model.AuditLog;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired // spring automatically injects the Repository dependency here
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepo) {
        this.auditLogRepository = auditLogRepo;
    }

    @Async // making this method asynchronous so that it does not block the main thread and improves performance of app while logging audit info
    public void logAudit(String action, Expense expense) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setExpenseId(expense.getId());
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDescription("Expense "+ action + " -Amount: "+ expense.getAmount());

        auditLogRepository.save(auditLog);
    }

}
