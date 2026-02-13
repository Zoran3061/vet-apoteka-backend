package com.metropolitan.pz;

import com.metropolitan.pz.controller.ProductController;
import com.metropolitan.pz.entities.Product;
import com.metropolitan.pz.entities.enums.Category;
import com.metropolitan.pz.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(createProduct(1L, "Product 1", "image1.jpg", Category.ANTIPARAZITICI, "Material 1", 10.0, "Description 1"));
        products.add(createProduct(2L, "Product 2", "image2.jpg", Category.SUPLEMENTI, "Material 2", 20.0, "Description 2"));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        Long id = 1L;
        Product product = createProduct(id, "Product 1", "image1.jpg", Category.ANTIPARAZITICI, "Material 1", 10.0, "Description 1");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productController.getProductById(id);

        assertEquals(product, result);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateProduct() {
        Product product = createProduct(1L, "Product 1", "image1.jpg", Category.ANTIPARAZITICI, "Material 1", 10.0, "Description 1");

        when(productRepository.save(product)).thenReturn(product);

        Product result = productController.createProduct(product);

        assertEquals(product, result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProduct() {
        Long id = 1L;
        Product existingProduct = createProduct(id, "Product 1", "image1.jpg", Category.ANTIPARAZITICI, "Material 1", 10.0, "Description 1");
        Product updatedProduct = createProduct(id, "Updated Product 1", "updated_image.jpg", Category.SUPLEMENTI, "Updated Material", 20.0, "Updated Description");

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);

        Product result = productController.updateProduct(id, updatedProduct);

        assertEquals(updatedProduct, result);
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;

        productController.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    private Product createProduct(Long id, String name, String image, Category category, String material, Double price, String description) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setImage(image);
        product.setCategory(category);
        product.setMaterial(material);
        product.setPrice(price);
        product.setDescription(description);
        return product;
    }
}
