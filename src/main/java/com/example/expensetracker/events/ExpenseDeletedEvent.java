package com.example.expensetracker.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDeletedEvent {
    private UUID eventId; // this one is going to be the eventId for each expense
    private UUID id; //expenseID
}
