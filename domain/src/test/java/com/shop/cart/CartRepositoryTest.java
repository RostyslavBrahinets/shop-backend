package com.shop.cart;

import com.shop.adminnumber.AdminNumberRepository;
import com.shop.configs.DatabaseConfig;
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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.shop.SqlMigrationClasspath.*;
import static com.shop.adminnumber.AdminNumberParameter.getAdminNumberWithoutId;
import static com.shop.cart.CartParameter.*;
import static com.shop.user.UserParameter.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
    DatabaseConfig.class
})
@Sql(scripts = {
    ADMIN_NUMBER,
    USER,
    CART
})
class CartRepositoryTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        AdminNumberRepository adminNumberRepository = new AdminNumberRepository(jdbcTemplate);
        adminNumberRepository.save(getAdminNumberWithoutId());

        UserRepository userRepository = new UserRepository(jdbcTemplate);
        userRepository.save(getUserWithoutId());
        userRepository.save(getUserWithoutId(getEmail2(), getPhone2()));

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
    @DisplayName("Find all carts")
    void find_all_carts() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            getMapOfEntries(getPriceAmount(), getUserId()),
            getMapOfEntries(getPriceAmount(), getUserId2())
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("price_amount", "user_id")
            .executeBatch(batchInsertParameters);

        List<Cart> carts = cartRepository.findAll();

        assertThat(carts).isEqualTo(
            List.of(
                getCartWithId(),
                getCartWithId2(getUserId2())
            )
        );
    }

    @Test
    @DisplayName("Cart by id was not found")
    void cart_by_id_was_not_found() {
        Optional<Cart> cart = cartRepository.findById(getCartId());

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by id was found")
    void cart_by_id_was_found() {
        insertTestDataToDb();

        Optional<Cart> cart = cartRepository.findById(getCartId());

        assertThat(cart).get().isEqualTo(getCartWithId());
    }

    @Test
    @DisplayName("Save cart")
    void save_cart() {
        cartRepository.save(getCartWithoutId());

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple carts")
    void save_multiple_carts() {
        cartRepository.save(getCartWithoutId());
        cartRepository.save(getCartWithoutId(getUserId2()));

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Update cart")
    void update_cart() {
        insertTestDataToDb();

        Optional<Cart> cart = cartRepository.update(getCartId(), getCartWithId(getPriceAmount2()));

        assertThat(cart).get().isEqualTo(getCartWithId(getPriceAmount2()));
    }

    @Test
    @DisplayName("Cart not deleted in case when not exists")
    void cart_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> cartRepository.delete(getCartWithoutId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete cart")
    void delete_cart() {
        insertTestDataToDb();

        var cartsCountBeforeDeletion = fetchCartsCount();

        assertThat(cartsCountBeforeDeletion).isEqualTo(1);

        cartRepository.delete(getCartWithId());

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isZero();
    }

    @Test
    @DisplayName("Cart by user was not found")
    void cart_by_user_was_not_found() {
        Optional<Cart> cart = cartRepository.findByUser(getUserWithId(getUserId()));

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by user was found")
    void cart_by_user_was_found() {
        insertTestDataToDb();

        Optional<Cart> cart = cartRepository.findByUser(getUserWithId());

        assertThat(cart).get().isEqualTo(getCartWithId());
    }

    private void insertTestDataToDb() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("price_amount", "user_id")
            .execute(getMapOfEntries(getPriceAmount(), getUserId()));
    }

    private static Map<String, Serializable> getMapOfEntries(double priceAmount, long userId) {
        return Map.ofEntries(
            Map.entry("price_amount", priceAmount),
            Map.entry("user_id", userId)
        );
    }
}
