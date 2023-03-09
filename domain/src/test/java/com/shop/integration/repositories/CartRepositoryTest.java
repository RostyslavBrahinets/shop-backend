package com.shop.integration.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.CartDao;
import com.shop.dao.PersonDao;
import com.shop.models.Cart;
import com.shop.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    CartRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/cart/V20220421161946__Create_table_cart.sql"
})
public class CartRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(namedParameterJdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE cart");
    }

    @Test
    @DisplayName("No carts in database")
    void no_history_records_in_db() {
        int cartsCount = cartRepository.count();

        assertThat(cartsCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing cart")
    void nothing_happened_when_trying_to_delete_not_existing_cart() {
        assertThatCode(() -> cartRepository.delete(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cart was deleted")
    @DirtiesContext
    void cart_was_deleted() {
        cartRepository.save(0, 1);

        assertThat(cartRepository.count()).isEqualTo(1);

        cartRepository.delete(cartRepository.count());

        assertThat(cartRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save cart and check cart data")
    @DirtiesContext
    void save_cart_and_check_cart_data() {
        cartRepository.save(0, 1);
        var savedCart = cartRepository.findById(cartRepository.count());
        Cart cart = null;
        if (savedCart.isPresent()) {
            cart = savedCart.get();
        }

        assertThat(cart).extracting(Cart::getId).isEqualTo(1L);
        assertThat(cart).extracting(Cart::getTotalCost).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Save multiple carts")
    @DirtiesContext
    void save_multiple_carts() {
        cartRepository.save(0, 1);
        cartRepository.save(0, 2);

        assertThat(cartRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Cart was not found")
    void cart_was_not_found() {
        Optional<Cart> cart = cartRepository.findById(1);

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart was found")
    @DirtiesContext
    void cart_was_found() {
        cartRepository.save(0, 1);

        Optional<Cart> cart = cartRepository.findById(cartRepository.count());

        assertThat(cart).get().isEqualTo(Cart.of(0).withId(1));
    }

    @Test
    @DisplayName("Find all carts")
    @DirtiesContext
    void find_all_carts() {
        cartRepository.save(0, 1);
        cartRepository.save(0, 2);

        var carts = cartRepository.findAll();

        assertThat(carts).isEqualTo(
            List.of(
                Cart.of(0).withId(1),
                Cart.of(0).withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public CartDao cartDao() {
            return new CartDao(namedParameterJdbcTemplate);
        }

        @Bean
        public CartRepository cartRepository(CartDao cartDao) {
            return new CartRepository(cartDao);
        }
    }
}
