package com.shkvlnc.bujo_app.repository;


import com.shkvlnc.bujo_app.domain.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByStatus(String status);
    List<Inbox> findByProject_Id(Long projectId);
    List<Inbox> findByContext_Id(Long contextId);
}