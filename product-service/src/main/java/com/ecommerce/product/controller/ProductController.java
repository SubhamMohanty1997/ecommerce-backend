package com.ecommerce.product.controller;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        Product savedProduct = productService.createProduct(product);
        log.info("Product created successfully with ID: {}",savedProduct.getId());
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        log.info("Fetching product with ID: {}",id);
        Product product = productService.getProductById(id);
        log.debug("Product fetched successfully with ID: {}",product.getId());
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts(){
        log.info("Fetching all products");
        List<Product> allProducts = productService.getAllProducts();
        log.debug("Total products found: {}",allProducts.size());
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        log.info("Deleting products with ID: {}",id);
        productService.deleteProductById(id);
        log.info("Product with ID: {} successfully deleted.",id);
        return new ResponseEntity<>("Product deleted with id: "+id, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@Valid @RequestBody Product product){
        log.info("Product updating with new data of ID: {}",id);
        Product updatedProduct = productService.updateProductById(id,product);
        log.debug("Product updated successfully with DATA: {} of ID: {}",updatedProduct,updatedProduct.getId());
        return new ResponseEntity<>(updatedProduct, HttpStatus.ACCEPTED);
    }

    @PostMapping("/createProducts")
    public ResponseEntity<List<Product>> createProducts(@Valid @RequestBody List<Product> products){
        log.info("Received request to create {} products", products.size());
        List<Product> savedProducts = productService.createProducts(products);
        log.debug("Successfully created {} products", savedProducts.size());
        return new ResponseEntity<>(savedProducts,HttpStatus.CREATED);
    }


}
