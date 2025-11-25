package com.shkvlnc.bujo_app.repository;

import com.shkvlnc.bujo_app.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameIgnoreCase(String name);
}