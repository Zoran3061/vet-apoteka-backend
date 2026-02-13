package com.metropolitan.pz.service;

import com.metropolitan.pz.entities.Product;
import com.metropolitan.pz.repository.ProductRepository;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product updatedProduct);

    void deleteProduct(Long id);
}
