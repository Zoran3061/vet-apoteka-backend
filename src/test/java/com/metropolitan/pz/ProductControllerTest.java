package com.metropolitan.pz;

import com.metropolitan.pz.controller.ProductController;
import com.metropolitan.pz.dto.ProductUpsertDto;
import com.metropolitan.pz.entities.Category;
import com.metropolitan.pz.entities.Product;
import com.metropolitan.pz.repository.CategoryRepository;
import com.metropolitan.pz.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {

        List<Product> products = new ArrayList<>();

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("LEKOVI");

        products.add(createProduct(1L, "Product 1", "image1.jpg", cat,
                "Material 1", 10.0, "Description 1", 5));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productController.getAllProducts(null, null);

        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {

        Long id = 1L;

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("LEKOVI");

        Product product = createProduct(id, "Product 1", "image1.jpg", cat,
                "Material 1", 10.0, "Description 1", 5);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productController.getProductById(id);

        assertEquals(product, result);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateProduct() {

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("LEKOVI");

        ProductUpsertDto dto = new ProductUpsertDto();
        dto.name = "Product 1";
        dto.image = "image1.jpg";
        dto.categoryId = 1L;
        dto.material = "Material 1";
        dto.price = 10.0;
        dto.description = "Description 1";
        dto.stock = 5;

        Product saved = createProduct(1L, dto.name, dto.image, cat,
                dto.material, dto.price, dto.description, dto.stock);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productController.createProduct(dto);

        assertEquals(saved, result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {

        Long id = 1L;

        Category oldCat = new Category();
        oldCat.setId(1L);
        oldCat.setName("LEKOVI");

        Category newCat = new Category();
        newCat.setId(2L);
        newCat.setName("SUPLEMENTI");

        Product existing = createProduct(id, "Old", "old.jpg", oldCat,
                "OldMat", 10.0, "OldDesc", 5);

        ProductUpsertDto dto = new ProductUpsertDto();
        dto.name = "New";
        dto.image = "new.jpg";
        dto.categoryId = 2L;
        dto.material = "NewMat";
        dto.price = 20.0;
        dto.description = "NewDesc";
        dto.stock = 10;

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCat));
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        Product result = productController.updateProduct(id, dto);

        assertEquals("New", result.getName());
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    public void testDeleteProduct() {

        Long id = 1L;

        productController.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    private Product createProduct(Long id, String name, String image,
                                  Category category, String material,
                                  Double price, String description,
                                  Integer stock) {

        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setImage(image);
        p.setCategory(category);
        p.setMaterial(material);
        p.setPrice(price);
        p.setDescription(description);
        p.setStock(stock);
        return p;
    }
}