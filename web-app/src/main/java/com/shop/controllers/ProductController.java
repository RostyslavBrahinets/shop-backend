package com.shop.controllers;

import com.shop.models.Product;
import com.shop.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ProductController.PRODUCTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    public static final String PRODUCTS_URL = "/web-api/products";
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Product findByIdProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public Product savePerson(
        @RequestBody Product product
    ) {
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }
}
