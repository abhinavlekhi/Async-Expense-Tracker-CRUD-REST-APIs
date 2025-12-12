package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ApiResponse;
import com.example.expensetracker.dto.ExpenseRequestDTO;
import com.example.expensetracker.dto.PaginationResponse;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired // basically we are injecting our service class here since controller does not know about repository directly
    private ExpenseService expenseService;

    // 1. Create - Add a new Expense
    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> addExpense(@Valid @RequestBody ExpenseRequestDTO dto) {
        Expense savedExpense = expenseService.addExpense(dto);
        return ResponseEntity.ok(new ApiResponse<>("Expense created successfully", savedExpense));
    }

    // 2. Read - get all expenses, here we are using optional query params to filter expenses by title and/or date
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) LocalDate date
//    ) {
//        List<Expense> expenses = expenseService.getAllExpense(title, date);
//        return ResponseEntity.ok(new ApiResponse<>("Fetched all expenses till date successfully", expenses));
//    }

    // 3. Read - get all expenses, here we are using pagination and sorting to cut down the records in the response payload
//    @GetMapping
//    public ResponseEntity<ApiResponse<?>> getAllExpenses1(
//            @RequestParam (defaultValue="0") int page,
//            @RequestParam (defaultValue="10") int size,
//            @RequestParam (defaultValue = "id, asc") String[] sort
//    ) {
//        String sortField = sort[0];
//        String sortDirection = sort.length > 1 ? sort[1] : "asc";
//
//        Sort springSort = sortDirection.equalsIgnoreCase("desc") ?
//                Sort.by(sortField).descending() :
//                Sort.by(sortField).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, springSort);
//
//        Page<Expense> expensePage = expenseService.getExpenses(pageable);
//        PaginationResponse pagination = new PaginationResponse(
//                expensePage.getContent(),
//                expensePage.getNumber(),
//                expensePage.getSize(),
//                expensePage.getTotalElements(),
//                expensePage.getTotalPages(),
//                expensePage.isLast()
//        );
//        return ResponseEntity.ok(new ApiResponse<>("Fetched all expenses successfully", pagination));
//    }

    // 3. Read - get certain expenses based on filter and pagination limit set, here we are using pagination and sorting to cut down the records in the response payload
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getFilteredExpensesBasedOnPagination(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort;
        if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Expense> expensePage = expenseService.getFilteredExpenses(title, date, pageable);
        PaginationResponse pagination = new PaginationResponse(
                expensePage.getContent(),
                expensePage.getNumber(),
                expensePage.getSize(),
                expensePage.getTotalElements(),
                expensePage.getTotalPages(),
                expensePage.isLast()
        );
        return ResponseEntity.ok(new ApiResponse<>("Fetched filtered expenses successfully", pagination));
    }

    // 4. Fetch - get expenses by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> getExpenseById(@PathVariable UUID id) {
        try {
            Expense expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(new ApiResponse<>("Expense fetched successfully", expense));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage()));
        }
    }

    // 5. Delete - delete particular expenses by it's id (UUID)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteExpenseById(@PathVariable UUID id) {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.ok(new ApiResponse<>("Expense deleted successfully"));
    }

    // 6. Patch - update a particular expense partially by its id (UUID), meaning without passing whole body, just pass whatever you want to update and it will do that
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpensePartially(@PathVariable UUID id, @Valid @RequestBody ExpenseRequestDTO dto) {
            Expense updatedExpense= expenseService.updateExpensePartially(id, dto);
            return ResponseEntity.ok(new ApiResponse<>("Expense updated successfully", updatedExpense));
    }
}
