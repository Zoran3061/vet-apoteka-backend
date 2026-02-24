package com.metropolitan.pz.controller;

import com.metropolitan.pz.entities.Category;
import com.metropolitan.pz.repository.CategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    // svi ulogovani mogu da vide kategorije (kupac treba za filter)
    @GetMapping
    public List<Category> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Category getOne(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    // ADMIN CRUD
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category create(@RequestBody Category c) {
        if (c.getName() == null || c.getName().trim().isEmpty())
            throw new RuntimeException("Category name is required");
        if (repo.existsByNameIgnoreCase(c.getName().trim()))
            throw new RuntimeException("Category already exists");

        c.setName(c.getName().trim());
        return repo.save(c);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category updated) {
        Category c = repo.findById(id).orElseThrow(() -> new RuntimeException("Category not found: " + id));
        if (updated.getName() == null || updated.getName().trim().isEmpty())
            throw new RuntimeException("Category name is required");
        c.setName(updated.getName().trim());
        return repo.save(c);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}