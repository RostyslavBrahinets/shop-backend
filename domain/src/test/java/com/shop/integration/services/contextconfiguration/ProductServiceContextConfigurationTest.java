package com.shop.integration.services.contextconfiguration;

import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
import com.shop.services.ProductService;
import com.shop.validators.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductService.class,
        ProductServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductServiceContextConfigurationTest {
    @Autowired
    private Product product;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("Get all products")
    void get_all_products() {
        productService.findAll();

        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Get product by id")
    void get_product_by_id() {
        long id = 1;

        productService.findById(id);

        verify(productValidator, atLeast(1)).validate(id);
        verify(productRepository).findById(id);
    }

    @Test
    @DisplayName("Get product by barcode")
    void get_product_by_barcode() {
        String barcode = "123";

        productService.findByBarcode(barcode);

        verify(productValidator, atLeast(1)).validate(barcode);
        verify(productRepository).findByBarcode(barcode);
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        productService.save(product);

        verify(productValidator, atLeast(1)).validate(product);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Delete product")
    void delete_product() {
        String barcode = "123";

        productService.delete(barcode);

        verify(productValidator, atLeast(1)).validate(barcode);
        verify(productRepository).delete(barcode);
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        byte[] image = {1, 1, 1};
        long id = 1;

        productService.saveImageForProduct(image, id);

        verify(productValidator, atLeast(1)).validate(id);
        verify(productRepository).saveImageForProduct(image, id);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Product product() {
            return mock(Product.class);
        }

        @Bean
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }

        @Bean
        public ProductValidator productValidator() {
            return mock(ProductValidator.class);
        }
    }
}
