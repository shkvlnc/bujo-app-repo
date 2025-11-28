package com.shkvlnc.bujo_app.repository;

import com.shkvlnc.bujo_app.domain.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByStatus(Inbox.Status status); // ✅ enum instead of String
    List<Inbox> findByProjectId(Long projectId);   // ✅ cleaner naming
    List<Inbox> findByContextId(Long contextId);   // ✅ cleaner naming
    List<Inbox> findByTitleContainingIgnoreCase(String keyword);
    List<Inbox> findByTags_NameIgnoreCase(String tag);

    @Query("""
        SELECT i FROM Inbox i
        WHERE (:keyword IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:tag IS NULL OR :tag IN elements(i.tags))
          AND (:status IS NULL OR i.status = :status)
          AND (:contextId IS NULL OR i.context.id = :contextId)
          AND (:projectId IS NULL OR i.project.id = :projectId)
    """)
    List<Inbox> search(@Param("keyword") String keyword,
                       @Param("tag") String tag,
                       @Param("status") Inbox.Status status,
                       @Param("contextId") Long contextId,
                       @Param("projectId") Long projectId);
}
