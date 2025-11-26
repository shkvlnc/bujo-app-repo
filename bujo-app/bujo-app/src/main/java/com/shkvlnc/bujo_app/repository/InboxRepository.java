package com.shkvlnc.bujo_app.repository;

import com.shkvlnc.bujo_app.domain.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByStatus(Inbox.Status status); // ✅ enum instead of String
    List<Inbox> findByProjectId(Long projectId);   // ✅ cleaner naming
    List<Inbox> findByContextId(Long contextId);   // ✅ cleaner naming
    List<Inbox> findByTitleContainingIgnoreCase(String keyword);
    List<Inbox> findByTags_NameIgnoreCase(String tag);
}
