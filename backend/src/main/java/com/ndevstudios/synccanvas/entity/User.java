package com.ndevstudios.synccanvas.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,name = "password_hash")
    private String passwordHash;

    @Column(nullable = false,name = "display_name")
    private String displayName;

    @Column(nullable = false)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
