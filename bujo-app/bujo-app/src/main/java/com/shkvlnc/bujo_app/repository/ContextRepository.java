package com.shkvlnc.bujo_app.repository;

import com.shkvlnc.bujo_app.domain.Context;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContextRepository extends JpaRepository<Context, Long> {

    Optional<Context> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Context> findByNameContainingIgnoreCase(String keyword);
}
