package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.dto.ProjectResponse;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projRepo;

    public ProjectController(ProjectRepository repo) { this.projRepo = repo; }

    // ✅ GET all projects as DTOs
    @GetMapping
    public List<ProjectResponse> list() {
        return projRepo.findAll().stream()
                .map(ProjectResponse::new)
                .toList();
    }

    // ✅ POST create project → return DTO
    @PostMapping
    public ProjectResponse create(@RequestBody Project p) {
        Project saved = projRepo.save(p);
        return new ProjectResponse(saved);
    }

    // ✅ PUT update project → return DTO
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id, @RequestBody Project p) {
        return projRepo.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setDescription(p.getDescription());
            Project updated = projRepo.save(existing);
            return ResponseEntity.ok(new ProjectResponse(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE project → no content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET single project with tasks/inboxes → return DTO
    @GetMapping("/{id}")
    public ProjectResponse getProjectWithTasks(@PathVariable Long id) {
        Project project = projRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return new ProjectResponse(project);
    }
}
