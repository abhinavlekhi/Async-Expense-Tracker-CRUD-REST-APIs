package com.example.expensetracker.specification;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExpenseSpecification {
    public static Specification<Expense> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("expenseTitle")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Expense> hasDate(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> {
            if (localDate == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("date"), localDate);
        };
    }
}
