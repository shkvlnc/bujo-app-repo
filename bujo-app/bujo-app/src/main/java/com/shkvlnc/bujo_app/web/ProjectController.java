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

    @GetMapping
    public List<ProjectResponse> list() {
        return projectService.listAll();
    }


    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }


    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectCreateRequest req) {
        ProjectResponse created = projectService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ProjectUpdateRequest req) {
        return projectService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
