package com.mboumda.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table( name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // Géré côté DB (DEFAULT CURRENT_TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;
}
