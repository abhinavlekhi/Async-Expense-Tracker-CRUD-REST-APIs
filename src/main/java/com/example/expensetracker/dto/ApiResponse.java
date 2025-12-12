package com.example.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// Here ApiResponse is a generic wrapper for all your responses, <T> means it can wrap any kind of data (Like expense, List<Expense>, or even null)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse(String message) {
        this.message=message;
    }

    public ApiResponse(String message, T data) {
        this.message=message;
        this.data=data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
