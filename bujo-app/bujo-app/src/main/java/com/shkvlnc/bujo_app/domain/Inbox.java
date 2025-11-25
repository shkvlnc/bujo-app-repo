package com.shkvlnc.bujo_app.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "inbox")
public class Inbox {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    private LocalDate dueDate;

    @Column
    private Integer priority; // 1-5

    @Column(length = 20)
    private String status = "PENDING"; // PENDING, DONE

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "context_id")
    private Context context;

    @ManyToMany
    @JoinTable(
            name = "inbox_tags",
            joinColumns = @JoinColumn(name = "inbox_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // getters/setters
}