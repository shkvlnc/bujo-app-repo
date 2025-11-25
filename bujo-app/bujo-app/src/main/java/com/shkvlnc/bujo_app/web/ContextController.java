package com.shkvlnc.bujo_app.web;



import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contexts")
public class ContextController {
    private final ContextRepository repo;
    public ContextController(ContextRepository repo) { this.repo = repo; }

    @GetMapping public List<Context> list() { return repo.findAll(); }
    @PostMapping public Context create(@RequestBody Context c) { return repo.save(c); }
    @GetMapping("/{id}") public ResponseEntity<Context> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}") public ResponseEntity<Context> update(@PathVariable Long id, @RequestBody Context c) {
        return repo.findById(id).map(existing -> {
            existing.setName(c.getName());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id); return ResponseEntity.noContent().build();
    }
}