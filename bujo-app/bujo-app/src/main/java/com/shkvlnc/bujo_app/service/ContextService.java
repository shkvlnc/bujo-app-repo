package com.shkvlnc.bujo_app.service;

import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.dto.ContextCreateRequest;
import com.shkvlnc.bujo_app.dto.ContextUpdateRequest;
import com.shkvlnc.bujo_app.dto.ContextResponse;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContextService {
    private final ContextRepository contextRepo;

    public List<ContextResponse> listAll() {
        return contextRepo.findAll().stream()
                .map(ContextResponse::fromEntity) // ✅ consistent DTO mapping
                .toList();
    }

    public ContextResponse getById(Long id) {
        Context context = contextRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Context not found"));
        return ContextResponse.fromEntity(context);
    }

    public ContextResponse create(ContextCreateRequest req) {
        if (contextRepo.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Context with this name already exists");
        }
        Context context = new Context();
        context.setName(req.getName());
        return ContextResponse.fromEntity(contextRepo.save(context));
    }

    public ContextResponse update(Long id, ContextUpdateRequest req) {
        Context existing = contextRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Context not found"));
        existing.setName(req.getName());
        return ContextResponse.fromEntity(contextRepo.save(existing));
    }

    public void delete(Long id) {
        if (!contextRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Context not found");
        }
        contextRepo.deleteById(id);
    }
}
