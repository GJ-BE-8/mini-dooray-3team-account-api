package com.nhnacademy.minidooray3team.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "Account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // status ENUM('ACTIVE', 'DORMANT', 'DELETED'),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, MEMBER

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updatedAt;


    public Account(String username, String email, String password, Status status, Role role, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
    }
}
