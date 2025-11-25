package com.shkvlnc.bujo_app.repository;


import com.shkvlnc.bujo_app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByNameIgnoreCase(String name);
}