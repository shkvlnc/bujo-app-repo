package com.shkvlnc.bujo_app.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "context")
public class Context {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    // getters/setters
}