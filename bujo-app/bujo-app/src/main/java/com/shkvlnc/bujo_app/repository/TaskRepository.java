package com.shkvlnc.bujo_app.repository;


import com.shkvlnc.bujo_app.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByProject_Id(Long projectId);
    List<Task> findByContext_Id(Long contextId);
}