package com.shop.integration.services.contextconfiguration;

import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositories.ProductCategoryRepository;
import com.shop.repositories.ProductRepository;
import com.shop.services.ProductService;
import com.shop.validators.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

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
    private ProductRepository productRepository;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductService productService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = List.of();
    }

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

        verify(productValidator, atLeast(1)).validate(id, products);
        verify(productRepository).findById(id);
    }

    @Test
    @DisplayName("Get product by barcode")
    void get_product_by_barcode() {
        String barcode = "123";

        productService.findByBarcode(barcode);

        verify(productValidator, atLeast(1)).validate(barcode, products);
        verify(productRepository, atLeast(1)).findByBarcode(barcode);
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        String name = "name";
        String describe = "describe";
        double price = 0;
        String barcode = "123";
        boolean inStock = true;
        byte[] image = {1, 1, 1};

        productService.save(name, describe, price, barcode, inStock, image);

        verify(productValidator, atLeast(1)).validate(name, describe, price, barcode, products);
        verify(productRepository).save(name, describe, price, barcode, inStock, image);
    }

    @Test
    @DisplayName("Delete product")
    void delete_product() {
        String barcode = "123";
        long productId = 1L;
        long categoryId = 1L;

        when(productRepository.findByBarcode(barcode))
            .thenReturn(Optional.of(
                Product.of(
                        "name",
                        "describe",
                        0,
                        "123",
                        true,
                        new byte[]{1, 1, 1}
                    )
                    .withId(1)
            ));

        when(productCategoryRepository.findCategoryForProduct(productId))
            .thenReturn(Optional.of(Category.of("name").withId(1)));

        productService.delete(barcode);

        verify(productValidator, atLeast(1)).validate(barcode, products);
        verify(productRepository, atLeast(1)).findByBarcode(barcode);
        verify(productCategoryRepository).findCategoryForProduct(productId);
        verify(productCategoryRepository).deleteProductFromCategory(productId, categoryId);
        verify(productRepository).delete(barcode);
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        byte[] image = {1, 1, 1};
        long id = 1;

        productService.saveImageForProduct(image, id);

        verify(productValidator, atLeast(1)).validate(id, products);
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

        @Bean
        public ProductCategoryRepository productCategoryRepository() {
            return mock(ProductCategoryRepository.class);
        }
    }
}
