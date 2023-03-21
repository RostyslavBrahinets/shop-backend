package com.shop.cart;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberRepository;
import com.shop.configs.DatabaseConfig;
import com.shop.user.User;
import com.shop.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/adminnumber/V20220421160504__Create_table_admin_number.sql",
    "classpath:db/migration/user/V20220421161642__Create_table_user.sql",
    "classpath:db/migration/cart/V20220421161946__Create_table_cart.sql"
})
public class CartRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        AdminNumberRepository adminNumberRepository = new AdminNumberRepository(jdbcTemplate);
        adminNumberRepository.save(AdminNumber.of("0"));

        UserRepository userRepository = new UserRepository(jdbcTemplate);

        String firstName = "John";
        String lastName = "Smith";
        String email1 = "test1@email.com";
        String phone1 = "+380000000001";
        String email2 = "test2@email.com";
        String phone2 = "+380000000002";
        String password = "password";
        long adminNumberId = 1;

        userRepository.save(
            User.of(
                firstName,
                lastName,
                email1,
                phone1,
                password,
                adminNumberId
            )
        );

        userRepository.save(
            User.of(
                firstName,
                lastName,
                email2,
                phone2,
                password,
                adminNumberId
            )
        );

        cartRepository = new CartRepository(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "cart", "\"user\"", "admin_number"
        );
    }

    private int fetchCartsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "cart"
        );
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        cartRepository.save(Cart.of(0, 1));

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple carts")
    void save_multiple_carts() {
        cartRepository.save(Cart.of(0, 1));
        cartRepository.save(Cart.of(0, 2));

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Cart by id was not found")
    void cart_by_id_was_not_found() {
        Optional<Cart> cart = cartRepository.findById(1);

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by id was found")
    void cart_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "user_id")
            .execute(Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("user_id", 1)
            ));

        Optional<Cart> cart = cartRepository.findById(1);

        assertThat(cart).get().isEqualTo(Cart.of(0, 1).withId(1));
    }

    @Test
    @DisplayName("Cart by user was not found")
    void cart_by_user_was_not_found() {
        Optional<Cart> cart = cartRepository.findByUser(User.of(null, null).withId(2));

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by user was found")
    void cart_by_user_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("user_id", 1)
                )
            );

        Optional<Cart> cart = cartRepository.findByUser(User.of(null, null).withId(1));

        assertThat(cart).get().isEqualTo(Cart.of(0, 1).withId(1));
    }

    @Test
    @DisplayName("Find all carts")
    void find_all_carts() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("user_id", 1)
            ),
            Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("user_id", 2)
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "user_id")
            .executeBatch(batchInsertParameters);

        List<Cart> carts = cartRepository.findAll();

        assertThat(carts).isEqualTo(
            List.of(
                Cart.of(0, 1).withId(1),
                Cart.of(0, 2).withId(2)
            )
        );
    }

    @Test
    @DisplayName("Cart not deleted in case when not exists")
    void cart_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> cartRepository.delete(Cart.of(0, 0).withId(1))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cart deleted")
    void cart_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("user_id", 1)
                )
            );

        var cartsCountBeforeDeletion = fetchCartsCount();

        assertThat(cartsCountBeforeDeletion).isEqualTo(1);

        cartRepository.delete(Cart.of(0, 1).withId(1));

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isZero();
    }

    @Test
    @DisplayName("Cart updated")
    void cart_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "user_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("user_id", 1)
                )
            );

        cartRepository.update(Cart.of(100, 1).withId(1));

        var updatedCart = jdbcTemplate.queryForObject(
            "SELECT total_cost FROM cart WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            Double.class
        );

        assertThat(updatedCart).isEqualTo(100);
    }
}
