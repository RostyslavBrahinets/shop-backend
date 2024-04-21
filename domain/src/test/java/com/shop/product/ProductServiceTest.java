package com.shop.product;

import com.shop.category.Category;
import com.shop.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.shop.category.CategoryParameter.getCategoryId;
import static com.shop.product.ProductParameter.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;
    @Mock
    private ProductCategoryRepository productCategoryRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productService = new ProductService(
            productRepository,
            productValidator,
            productCategoryRepository
        );
    }

    @Test
    @DisplayName("Empty list of products is returned in case when no products in storage")
    void empty_list_of_products_is_returned_in_case_when_no_products_in_storage() {
        when(productRepository.findAll()).thenReturn(emptyList());

        List<Product> products = productService.findAll();

        verify(productRepository).findAll();

        assertThat(products).isEmpty();
    }

    @Test
    @DisplayName("List of products is returned in case when products are exists in storage")
    void list_of_products_is_returned_in_case_when_products_are_exists_in_storage() {
        when(productRepository.findAll())
            .thenReturn(
                List.of(getProductWithId())
            );

        List<Product> products = productService.findAll();

        verify(productRepository).findAll();

        assertThat(products).isEqualTo(
            List.of(getProductWithId())
        );
    }

    @Test
    @DisplayName("Product was found by id")
    void product_was_found_by_id() {
        when(productRepository.findById(getProductId()))
            .thenReturn(
                Optional.of(getProductWithId())
            );

        Product product = productService.findById(getProductId());

        verify(productRepository).findAll();
        verify(productValidator).validate(getProductId(), getProducts());
        verify(productRepository).findById(getProductId());

        assertThat(product).isEqualTo(getProductWithId());
    }

    @Test
    @DisplayName("Product was saved for with correct input")
    void product_was_saved_with_correct_input() {
        Product savedProduct = productService.save(getProductWithoutId());

        verify(productRepository, atLeast(1)).findAll();
        verify(productValidator).validate(
            getName(),
            getDescribe(),
            getPrice(),
            getBarcode(),
            getProducts()
        );
        verify(productRepository).save(getProductWithId());
        verify(productRepository, atLeast(1)).findAll();

        assertThat(savedProduct).isEqualTo(getProductWithId());
    }

    @Test
    @DisplayName("Product was updated for with correct input")
    void product_was_updated_with_correct_input() {
        Product updatedProduct = productService.update(
            getProductId(),
            getProductWithoutId2()
        );

        verify(productRepository, atLeast(1)).findAll();
        verify(productValidator).validate(getProductId(), getProducts());
        verify(productValidator).validateUpdatedProduct(getProductWithId2());
        verify(productRepository).update(
            getProductId(),
            getProductWithId2()
        );

        assertThat(updatedProduct).isEqualTo(getProductWithId2());
    }

    @Test
    @DisplayName("Product was deleted")
    void product_was_deleted() {
        when(productRepository.findByBarcode(getBarcode()))
            .thenReturn(Optional.of(getProductWithId()));

        when(productCategoryRepository.findCategoryForProduct(getProductId()))
            .thenReturn(
                Optional.of(Category.of(getName()).withId(getCategoryId()))
            );

        productService.delete(getProductWithoutId());

        verify(productRepository).findAll();
        verify(productValidator, atLeast(1)).validateBarcode(getBarcode(), getProducts());
        verify(productRepository).findByBarcode(getBarcode());
        verify(productCategoryRepository).findCategoryForProduct(getProductId());
        verify(productCategoryRepository).deleteProductFromCategory(getProductId(), getCategoryId());
        verify(productRepository).delete(getProductWithoutId());
    }

    @Test
    @DisplayName("Product was found by barcode")
    void product_was_found_by_name() {
        when(productRepository.findByBarcode(getBarcode())).thenReturn(
            Optional.of(getProductWithId())
        );

        Product product = productService.findByBarcode(getBarcode());

        verify(productRepository).findAll();
        verify(productValidator, atLeast(1)).validateBarcode(getBarcode(), getProducts());
        verify(productRepository).findByBarcode(getBarcode());

        assertThat(product).isEqualTo(getProductWithId());
    }

    @Test
    @DisplayName("Image for product was saved")
    void image_for_product_was_saved_with_correct_input() {
        productService.saveImageForProduct(getImage(), getProductId());

        verify(productRepository).findAll();
        verify(productValidator).validate(getProductId(), getProducts());
        verify(productRepository).saveImageForProduct(getImage(), getProductId());
    }


}
