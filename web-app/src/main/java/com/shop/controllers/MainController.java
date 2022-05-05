package com.shop.controllers;

import com.shop.models.Product;
import com.shop.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = MainController.MAIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    public static final String MAIN_URL = "/web-api";
    private final ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> index() {
        return productService.getRandomProducts(10);
    }
}

