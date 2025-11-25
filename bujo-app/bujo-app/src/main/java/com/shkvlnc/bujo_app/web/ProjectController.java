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

    @GetMapping public List<Project> list() { return projRepo.findAll(); }

    @PostMapping public Project create(@RequestBody Project p) { return projRepo.save(p); }

//    @GetMapping("/{id}") public ResponseEntity<Project> get(@PathVariable Long id) {
//        return projRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }

    @PutMapping("/{id}") public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project p) {
        return projRepo.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setDescription(p.getDescription());
            return ResponseEntity.ok(projRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        projRepo.deleteById(id); return ResponseEntity.noContent().build();
    }

    // ✅ GET /api/projects/{id} → project + its tasks
    @GetMapping("/{id}")
    public ProjectResponse getProjectWithTasks(@PathVariable Long id) {
        Project project = projRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return new ProjectResponse(project);
    }
}
