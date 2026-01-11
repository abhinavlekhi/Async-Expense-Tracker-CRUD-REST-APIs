package com.example.expensetracker.repository;

import com.example.expensetracker.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository layer which is responsible for all database interactions related to Audit_log table
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
