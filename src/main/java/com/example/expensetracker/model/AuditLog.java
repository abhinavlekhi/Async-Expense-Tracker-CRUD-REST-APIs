package com.example.expensetracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable= false, nullable=false)
    private UUID id;
    private String action;
    @Column(name="expense_id", nullable = false)
    private UUID expenseId;
    private String description;

    private LocalDateTime timestamp= LocalDateTime.now();
}
