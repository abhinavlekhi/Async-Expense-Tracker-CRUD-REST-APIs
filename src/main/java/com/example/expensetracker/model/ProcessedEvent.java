package com.example.expensetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name= "processed_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEvent {
    @Id
    private UUID eventId;

    @Column(nullable=false)
    private String eventType;

    @Column(nullable=false)
    private LocalDateTime processedAt = LocalDateTime.now();

//    public ProcessedEvent(UUID eventId, String eventType) {
//        this.eventId=eventId;
//        this.eventType=eventType;
//    }

}
