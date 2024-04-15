package com.shop.product;

import com.shop.category.Category;
import com.shop.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.shop.product.ProductParameter.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductService.class,
        ProductServiceContextConfigurationTest.TestContextConfig.class
    }
)
class ProductServiceContextConfigurationTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
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
        productService.findById(getProductId());

        verify(productValidator, atLeast(1))
            .validate(getProductId(), getProducts());
        verify(productRepository)
            .findById(getProductId());
    }

    @Test
    @DisplayName("Get product by barcode")
    void get_product_by_barcode() {
        productService.findByBarcode(getBarcode());

        verify(productValidator, atLeast(1))
            .validateBarcode(getBarcode(), getProducts());
        verify(productRepository, atLeast(1))
            .findByBarcode(getBarcode());
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        productService.save(getProductWithoutId());

        verify(productValidator, atLeast(1))
            .validate(
                getName(),
                getDescribe(),
                getPrice(),
                getBarcode(),
                getProducts()
            );
        verify(productRepository).save(getProductWithId(getProductId(), getBarcode()));
    }

    @Test
    @DisplayName("Delete product")
    void delete_product() {
        when(productRepository.findByBarcode(getBarcode()))
            .thenReturn(Optional.of(getProductWithId(getProductId(), getBarcode())));

        when(productCategoryRepository.findCategoryForProduct(getProductId()))
            .thenReturn(Optional.of(Category.of(getName()).withId(getProductId())));

        productService.delete(Product.of(getBarcode()));

        verify(productValidator, atLeast(1))
            .validateBarcode(getBarcode(), getProducts());
        verify(productRepository, atLeast(1))
            .findByBarcode(getBarcode());
        verify(productCategoryRepository)
            .findCategoryForProduct(getProductId());
        verify(productCategoryRepository)
            .deleteProductFromCategory(getProductId(), getCategoryId());
        verify(productRepository)
            .delete(Product.of(getBarcode()));
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        productService.saveImageForProduct(getImage(), getProductId());

        verify(productValidator, atLeast(1))
            .validate(getProductId(), getProducts());
        verify(productRepository)
            .saveImageForProduct(getImage(), getProductId());
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
