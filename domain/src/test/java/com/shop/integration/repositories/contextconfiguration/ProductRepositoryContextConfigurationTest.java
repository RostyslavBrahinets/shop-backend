package com.shop.integration.repositories.contextconfiguration;

import com.shop.dao.ProductDao;
import com.shop.models.Product;
import com.shop.repositories.ProductRepository;
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
        ProductRepository.class,
        ProductRepositoryContextConfigurationTest.TestContextConfig.class
    }
)
public class ProductRepositoryContextConfigurationTest {
    @Autowired
    private Product product;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Get all products")
    void get_all_products() {
        productRepository.findAll();

        verify(productDao).findAll();
    }

    @Test
    @DisplayName("Get product by id")
    void get_product_by_id() {
        long id = 1;

        productRepository.findById(id);

        verify(productDao).findById(id);
    }

    @Test
    @DisplayName("Get product by barcode")
    void get_product_by_person() {
        String barcode = "123";

        productRepository.findByBarcode(barcode);

        verify(productDao).findByBarcode(barcode);
    }

    @Test
    @DisplayName("Save product")
    void save_product() {
        productRepository.save(product);

        verify(productDao).save(
            product.getName(),
            product.getDescribe(),
            product.getPrice(),
            product.getBarcode(),
            product.isInStock(),
            product.getImage()
        );
    }

    @Test
    @DisplayName("Save image for product")
    void save_image_for_product() {
        byte[] image = {1, 1, 1};
        long id = 1;

        productRepository.saveImageForProduct(image, id);

        verify(productDao).saveImageForProduct(image, id);
    }

    @Test
    @DisplayName("Count products")
    void count_products() {
        productRepository.count();

        verify(productDao, atLeast(1)).findAll();
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public Product product() {
            return mock(Product.class);
        }

        @Bean
        public ProductDao productDao() {
            return mock(ProductDao.class);
        }
    }
}
