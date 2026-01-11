package com.example.expensetracker;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableCaching //This annotation enables cache support in application
@EnableScheduling
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

//    @Bean
//    public CommandLineRunner demo(ExpenseRepository expenseRepository) {
//        return args -> {
//            // Create a new Expense object
//            Expense expense = Expense.builder()
//                    .expenseTitle("Coffee at Starbucks")
//                    .amount(new BigDecimal("5.75"))
//                    .notes("Morning latte before work")
//                    .date(LocalDate.now())
//                    .build();
//
//            // Save it to the database
//            expenseRepository.save(expense);
//
//            // Print all expenses
//            expenseRepository.findAll().forEach(System.out::println);
//        };
//    }

}
