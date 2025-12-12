package com.example.expensetracker.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data  //generates getters, setters, toString, equals, hashCode
@Entity //tells spring that class maps to a DB table
@Table(name = "users") //specifying the table name
@NoArgsConstructor //generates constructors
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable=false)
    private String password;
}
