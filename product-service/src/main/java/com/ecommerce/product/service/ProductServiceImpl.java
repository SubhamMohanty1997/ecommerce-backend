package com.ecommerce.product.service;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID : {}",savedProduct.getId());
        return savedProduct;
    }
    @Cacheable(value = "products", key = "#id")
    @Override
    public Product getProductById(Long id) {
        log.info("Fetching product with ID: {}",id);
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found with ID: "+id));
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.debug("Total products found={}",products.size());
        return products;
    }
    @CacheEvict(value = "products", key = "#id")
    @Override
    public void deleteProductById(Long id) {
        log.info("Deleting product with ID: {}",id);
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found with ID: "+id));
        productRepository.deleteById(id);
        log.info("product deleted successfully with ID: {}",id);
    }
    @CachePut(value = "products", key = "#result.id")
    @Override
    public Product updateProductById(Long id, Product product) {
        log.info("product updating with new data of ID: {}",id);
        Product existing = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found with ID: "+id));
        existing.setId(product.getId());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        Product updated = productRepository.save(existing);
        log.info("product successfully updated of ID: {}",id);
        return updated;
    }

    @Override
    public List<Product> createProducts(List<Product> products) {
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts;
    }
}
