package com.metropolitan.pz.controller;

import com.metropolitan.pz.dto.ProductUpsertDto;
import com.metropolitan.pz.entities.Category;
import com.metropolitan.pz.entities.Product;
import com.metropolitan.pz.repository.CategoryRepository;
import com.metropolitan.pz.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductController(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    // svi ulogovani vide proizvode
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search
    ) {
        // privremeno: samo osnovno
        // Kasnije dodamo metode u repository za filter + search
        List<Product> all = productRepo.findAll();

        if (categoryId != null) {
            all = all.stream()
                    .filter(p -> p.getCategory() != null && categoryId.equals(p.getCategory().getId()))
                    .toList();
        }

        if (search != null && !search.trim().isEmpty()) {
            String s = search.trim().toLowerCase();
            all = all.stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(s))
                    .toList();
        }

        return all;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // ADMIN create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product createProduct(@RequestBody ProductUpsertDto dto) {
        Category category = categoryRepo.findById(dto.categoryId)
                .orElseThrow(() -> new RuntimeException("Invalid categoryId: " + dto.categoryId));

        Product p = new Product();
        p.setName(dto.name);
        p.setImage(dto.image);
        p.setCategory(category);
        p.setMaterial(dto.material);
        p.setPrice(dto.price);
        p.setDescription(dto.description);
        p.setStock(dto.stock != null ? dto.stock : 0);

        return productRepo.save(p);
    }

    //  ADMIN update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductUpsertDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (dto.categoryId != null) {
            Category category = categoryRepo.findById(dto.categoryId)
                    .orElseThrow(() -> new RuntimeException("Invalid categoryId: " + dto.categoryId));
            p.setCategory(category);
        }

        if (dto.name != null) p.setName(dto.name);
        p.setImage(dto.image);
        p.setMaterial(dto.material);
        if (dto.price != null) p.setPrice(dto.price);
        p.setDescription(dto.description);
        if (dto.stock != null) p.setStock(dto.stock);

        return productRepo.save(p);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
    }

    // MAGACIONER i ADMIN mogu da evidentiraju prijem robe
    @PreAuthorize("hasAnyRole('MAGACIONER','ADMIN')")
    @PutMapping("/{id}/increase-stock")
    public Product increaseStock(
            @PathVariable Long id,
            @RequestParam Integer amount
    ) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Količina mora biti veća od 0.");
        }

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock(product.getStock() + amount);

        return productRepo.save(product);
    }
}