package com.shop.productscategory;

import com.shop.category.CategoryParameter;
import com.shop.category.CategoryService;
import com.shop.category.CategoryValidator;
import com.shop.product.ProductService;
import com.shop.product.ProductValidator;
import com.shop.productcategory.ProductCategoryRepository;
import com.shop.productcategory.ProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.shop.category.CategoryParameter.*;
import static com.shop.product.ProductParameter.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ProductCategoryService.class,
        ProductCategoryServiceContextConfigurationTest.TestContextConfig.class
    }
)
class ProductCategoryServiceContextConfigurationTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("Get all products in category")
    void get_all_products_in_category() {
        productCategoryService.findAllProductsInCategory(getCategoryId());

        verify(categoryService, atLeast(1)).findAll();
        verify(categoryValidator, atLeast(1)).validate(getCategoryId(), getCategories());
        verify(productCategoryRepository).findAllProductsInCategory(getCategoryId());
    }

    @Test
    @DisplayName("Save product to category")
    void save_product_to_category() {
        when(productService.findByBarcode(getBarcode()))
            .thenReturn(getProductWithId());

        when(categoryService.findByName(CategoryParameter.getName()))
            .thenReturn(getCategoryWithId());

        productCategoryService.saveProductToCategory(getBarcode(), CategoryParameter.getName());

        verify(productService, atLeast(1)).findByBarcode(getBarcode());
        verify(categoryService, atLeast(1)).findByName(CategoryParameter.getName());
        verify(productCategoryRepository).saveProductToCategory(getProductId(), getCategoryId());
    }

    @Test
    @DisplayName("Update category for product")
    void update_category_for_product() {
        when(productService.findByBarcode(getBarcode()))
            .thenReturn(getProductWithId());

        when(categoryService.findByName(CategoryParameter.getName2()))
            .thenReturn(getCategoryWithId(getCategoryId(), getName2()));

        productCategoryService.updateCategoryForProduct(getBarcode(), CategoryParameter.getName2());

        verify(productService, atLeast(1)).findByBarcode(getBarcode());
        verify(categoryService, atLeast(1)).findByName(CategoryParameter.getName2());
        verify(productCategoryRepository).updateCategoryForProduct(getProductId(), getCategoryId());
    }

    @Test
    @DisplayName("Delete product from category")
    void delete_product_from_category() {
        when(productService.findByBarcode(getBarcode()))
            .thenReturn(getProductWithId());

        when(productCategoryRepository.findCategoryForProduct(getProductId()))
            .thenReturn(Optional.of(getCategoryWithId()));

        productCategoryService.deleteProductFromCategory(getBarcode());

        verify(productService, atLeast(1)).findAll();
        verify(productValidator, atLeast(1)).validateBarcode(
            getBarcode(),
            getProducts()
        );
        verify(productService, atLeast(1)).findByBarcode(getBarcode());
        verify(productService, atLeast(1)).findByBarcode(getBarcode());
        verify(productCategoryRepository, atLeast(1))
            .findCategoryForProduct(getProductId());
        verify(productCategoryRepository).deleteProductFromCategory(getProductId(), getCategoryId());
    }

    @Test
    @DisplayName("Find category for product")
    void find_category_for_product() {
        when(productService.findByBarcode(getBarcode()))
            .thenReturn(getProductWithId());

        when(productCategoryRepository.findCategoryForProduct(getProductId()))
            .thenReturn(Optional.of(getCategoryWithId()));

        productCategoryService.findCategoryForProduct(getBarcode());

        verify(productService, atLeast(1)).findAll();
        verify(productValidator, atLeast(1)).validateBarcode(
            getBarcode(),
            getProducts()
        );
        verify(productService, atLeast(1)).findByBarcode(getBarcode());
        verify(productCategoryRepository, atLeast(1))
            .findCategoryForProduct(getProductId());
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
