package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.dto.TaskCreateRequest;
import com.shkvlnc.bujo_app.dto.TaskResponse;
import com.shkvlnc.bujo_app.dto.TaskUpdateRequest;
import com.shkvlnc.bujo_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inbox")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @GetMapping public List<TaskResponse> list() { return taskService.listAll(); }
    @GetMapping("/status/{status}") public List<TaskResponse> listByStatus(@PathVariable String status) {
        return taskService.listByStatus(status);
    }
    @PostMapping public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest req) {
        return ResponseEntity.ok(taskService.create(req));
    }
    @PutMapping("/{id}") public ResponseEntity<TaskResponse> update(@PathVariable Long id,
                                                                    @Valid @RequestBody TaskUpdateRequest req) {
        return ResponseEntity.ok(taskService.update(id, req));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id); return ResponseEntity.noContent().build();
    }
}