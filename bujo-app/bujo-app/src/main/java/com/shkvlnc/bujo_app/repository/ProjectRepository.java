package com.shkvlnc.bujo_app.repository;


import com.shkvlnc.bujo_app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {}