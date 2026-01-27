package com.ecommerce.product.service;

import com.ecommerce.product.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    void deleteProductById(Long id);
    Product updateProductById(Long id,Product product);
}
