package com.shop.main;

import com.shop.product.Product;
import com.shop.product.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(MainController.MAIN_URL)
public class MainController {
    public static final String MAIN_URL = "/api/v1";
    private final ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> index() {
        return productService.findRandomProducts(10);
    }
}

