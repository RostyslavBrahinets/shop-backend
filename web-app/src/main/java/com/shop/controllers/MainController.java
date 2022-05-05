package com.shop.controllers;

import com.shop.configs.AppConfig;
import com.shop.models.Product;
import com.shop.services.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = MainController.MAIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    public static final String MAIN_URL = "/web-api";
    public static final AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext(AppConfig.class);
    public static final ProductService productService =
        applicationContext.getBean(ProductService.class);

    @GetMapping
    public List<Product> index() {
        List<Product> randomProducts = productService.getRandomProducts(10);
        System.out.println(randomProducts);
        return randomProducts;
    }
}

