package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository repo;
    public ProjectController(ProjectRepository repo) { this.repo = repo; }

    @GetMapping public List<Project> list() { return repo.findAll(); }
    @PostMapping public Project create(@RequestBody Project p) { return repo.save(p); }
    @GetMapping("/{id}") public ResponseEntity<Project> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}") public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project p) {
        return repo.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setDescription(p.getDescription());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id); return ResponseEntity.noContent().build();
    }
}
