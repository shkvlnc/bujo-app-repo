package com.shkvlnc.bujo_app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inboxes") // ✅ plural for convention
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)   // ✅ enforce non-null priority
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id")
    private Context context;

    @ManyToMany
    @JoinTable(
            name = "inbox_tags",
            joinColumns = @JoinColumn(name = "inbox_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // ✅ Ensure defaults before persisting
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.PENDING;
        }
        if (priority == null) {
            priority = Priority.MEDIUM; // sensible default
        }
    }

    // --- Enums ---
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT,
        CRITICAL
    }

    public enum Status {
        PENDING,
        DONE
    }
}
