package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.project.ProjectResponse;
import com.shkvlnc.bujo_app.dto.project.ProjectCreateRequest;
import com.shkvlnc.bujo_app.dto.project.ProjectUpdateRequest;
import com.shkvlnc.bujo_app.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    // ✅ GET all projects
    @GetMapping
    public List<ProjectResponse> list() {
        return projectService.listAll();
    }

    // ✅ GET single project
    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    // ✅ POST create project
    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectCreateRequest req) {
        ProjectResponse created = projectService.create(req);
        return ResponseEntity.status(201).body(created); // ✅ 201 Created
    }

    // ✅ PUT update project
    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ProjectUpdateRequest req) {
        return projectService.update(id, req); // ✅ simplified, service throws if not found
    }

    // ✅ DELETE project
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id); // ✅ service throws if not found
        return ResponseEntity.noContent().build();
    }
}
