package com.example.expensetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity //tells spring that class maps to a DB table
@Table(name = "expenses") //specifying the table name
@Data //generates getters, setters, toString, equals, hashCode
@NoArgsConstructor //generates constructors
@AllArgsConstructor //generates constructors
@Builder //lets you create objects cleanly using builder pattern.
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;   // here id is a primary key where it will be generated using postgres UUID

    @NotBlank(message = "Expense title is required")
    @Column(nullable=false)
    private String expenseTitle;  // required column

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Column(nullable=false)
    private BigDecimal amount;  // required column

    private String notes;

    @NotNull(message = "date is required")
    @Column(nullable=false)
    private LocalDate date;  // required column
}
