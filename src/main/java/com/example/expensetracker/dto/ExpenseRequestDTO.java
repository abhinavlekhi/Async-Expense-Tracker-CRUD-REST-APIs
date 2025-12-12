package com.example.expensetracker.dto;

import com.example.expensetracker.validation.NoNumbers;
import com.example.expensetracker.validation.UniqueTitle;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {

    @NotBlank(message = "Expense title cannot be blank")
    @Size(min=3, max=50, message="Expense title must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message="Expense title can only contain letters and spaces")
    @NoNumbers
    @UniqueTitle
    private String expenseTitle;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message="Amount must be greater than 0")
    private BigDecimal amount;

    private String notes;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

}
