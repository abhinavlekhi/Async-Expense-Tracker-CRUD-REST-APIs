package com.example.expensetracker.repository;

import com.example.expensetracker.model.DlqEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DlqEventRepository extends JpaRepository<DlqEvent, Long> {

}
