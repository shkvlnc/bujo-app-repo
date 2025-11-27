package com.shkvlnc.bujo_app.service;

import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.dto.project.ProjectCreateRequest;
import com.shkvlnc.bujo_app.dto.project.ProjectUpdateRequest;
import com.shkvlnc.bujo_app.dto.project.ProjectResponse;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepo;

    public List<ProjectResponse> listAll() {
        return projectRepo.findAll().stream()
                .map(ProjectResponse::fromEntity) // ✅ consistent DTO mapping
                .toList();
    }

    public ProjectResponse getById(Long id) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return ProjectResponse.fromEntity(project);
    }

    public ProjectResponse create(ProjectCreateRequest req) {
        if (projectRepo.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with this name already exists");
        }
        Project project = new Project();
        project.setName(req.getName());
        project.setDescription(req.getDescription());
        return ProjectResponse.fromEntity(projectRepo.save(project));
    }

    public ProjectResponse update(Long id, ProjectUpdateRequest req) {
        Project existing = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        existing.setName(req.getName());
        existing.setDescription(req.getDescription());
        return ProjectResponse.fromEntity(projectRepo.save(existing));
    }

    public void delete(Long id) {
        if (!projectRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        projectRepo.deleteById(id);
    }
}
