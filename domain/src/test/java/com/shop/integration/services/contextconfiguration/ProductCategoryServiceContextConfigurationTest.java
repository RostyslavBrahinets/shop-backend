package com.shop.integration.services.contextconfiguration;

import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositories.ProductCategoryRepository;
import com.shop.services.CategoryService;
import com.shop.services.ProductCategoryService;
import com.shop.services.ProductService;
import com.shop.validators.CategoryValidator;
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
        ProductCategoryService.class,
        ProductCategoryServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductCategoryServiceContextConfigurationTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("Get all products in category")
    void get_all_products_in_category() {
        long categoryId = 1;

        productCategoryService.findAllProductsInCategory(categoryId);

        verify(categoryValidator, atLeast(1)).validate(categoryId);
        verify(productCategoryRepository).findAllProductsInCategory(categoryId);
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        String barcode = "123";
        String categoryName = "name";

        when(productService.findByBarcode(barcode))
            .thenReturn(Product.of(
                    "name",
                    "describe",
                    0,
                    barcode,
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1));

        when(categoryService.findByName(categoryName))
            .thenReturn(Category.of(categoryName).withId(1));

        productCategoryService.saveProductToCategory(barcode, categoryName);

        verify(productValidator, atLeast(1)).validate(barcode);
        verify(categoryValidator, atLeast(1)).validate(categoryName);
        verify(productCategoryRepository).saveProductToCategory(1, 1);
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public ProductCategoryRepository productCategoryRepository() {
            return mock(ProductCategoryRepository.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }

        @Bean
        public CategoryService categoryService() {
            return mock(CategoryService.class);
        }

        @Bean
        public ProductValidator productValidator() {
            return mock(ProductValidator.class);
        }

        @Bean
        public CategoryValidator categoryValidator() {
            return mock(CategoryValidator.class);
        }
    }
}
