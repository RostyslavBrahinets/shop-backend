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
    @DisplayName("Product was saved for with correct input")
    void product_was_saved_with_correct_input() {
        Product savedProduct = productService.save(
            Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );

        verify(productRepository).save(
            Product.of(
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                )
                .withId(1)
        );

        assertThat(savedProduct).isEqualTo(
            new Product(
                1,
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            )
        );
    }

    @Test
    @DisplayName("Empty list of products is returned in case when no products in storage")
    void empty_list_of_products_is_returned_in_case_when_no_products_in_storage() {
        when(productRepository.findAll()).thenReturn(emptyList());

        List<Product> products = productService.findAll();

        assertThat(products).isEmpty();
    }

    @Test
    @DisplayName("List of products is returned in case when products are exists in storage")
    void list_of_products_is_returned_in_case_when_products_are_exists_in_storage() {
        when(productRepository.findAll()).thenReturn(
            List.of(
                Product.of(
                    "name",
                    "describe",
                    100,
                    "123",
                    true,
                    new byte[]{1, 1, 1}
                ).withId(1)
            )
        );

        List<Product> products = productService.findAll();

        assertThat(products).isEqualTo(List.of(new Product(
            1,
            "name",
            "describe",
            100,
            "123",
            true,
            new byte[]{1, 1, 1})
        ));
    }

    @Test
    @DisplayName("Product was found by id")
    void product_was_found_by_id() {
        when(productRepository.findById(1)).thenReturn(
            Optional.of(Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            ).withId(1))
        );

        Product product = productService.findById(1);

        assertThat(product).isEqualTo(new Product(
            1,
            "name",
            "describe",
            100,
            "123",
            true,
            new byte[]{1, 1, 1})
        );
    }

    @Test
    @DisplayName("Product was found by barcode")
    void product_was_found_by_name() {
        when(productRepository.findByBarcode("123")).thenReturn(
            Optional.of(Product.of(
                "name",
                "describe",
                100,
                "123",
                true,
                new byte[]{1, 1, 1}
            ).withId(1))
        );

        Product product = productService.findByBarcode("123");

        assertThat(product).isEqualTo(new Product(
            1,
            "name",
            "describe",
            100,
            "123",
            true,
            new byte[]{1, 1, 1})
        );
    }

    @Test
    @DisplayName("Product was deleted")
    void product_was_deleted() {
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

        productService.delete(Product.of(barcode));

        verify(productValidator, atLeast(1)).validateBarcode(barcode, List.of());
        verify(productRepository).findByBarcode(barcode);
        verify(productCategoryRepository).findCategoryForProduct(productId);
        verify(productCategoryRepository).deleteProductFromCategory(productId, categoryId);
        verify(productRepository).delete(Product.of(barcode));
    }

    @Test
    @DisplayName("Image for product was saved")
    void image_for_product_was_saved_with_correct_input() {
        productService.saveImageForProduct(new byte[]{1, 1, 1}, 1);
        verify(productRepository).saveImageForProduct(new byte[]{1, 1, 1}, 1);
    }
}
