package com.shkvlnc.bujo_app.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contexts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Context {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description; // optional metadata for richer context info
}
