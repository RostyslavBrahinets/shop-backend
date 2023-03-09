package com.shop.integration.dao;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.CartDao;
import com.shop.dao.PersonDao;
import com.shop.models.Cart;
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
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/cart/V20220421161946__Create_table_cart.sql"
})
public class CartDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private CartDao cartDao;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(jdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        cartDao = new CartDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "cart", "person"
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
        cartDao.save(0, 1);

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple carts")
    void save_multiple_carts() {
        cartDao.save(0, 1);
        cartDao.save(0, 2);

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Cart by id was not found")
    void cart_by_id_was_not_found() {
        Optional<Cart> cart = cartDao.findById(1);

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by id was found")
    void cart_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("person_id", 1)
            ));

        Optional<Cart> cart = cartDao.findById(1);

        assertThat(cart).get().isEqualTo(Cart.of(0).withId(1));
    }

    @Test
    @DisplayName("Cart by person was not found")
    void cart_by_person_was_not_found() {
        Optional<Cart> cart = cartDao.findByPerson(1);

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("Cart by person was found")
    void cart_by_person_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("person_id", 1)
                )
            );

        Optional<Cart> cart = cartDao.findByPerson(1);

        assertThat(cart).get().isEqualTo(Cart.of(0).withId(1));
    }

    @Test
    @DisplayName("Find all carts")
    void find_all_carts() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("person_id", 1)
            ),
            Map.ofEntries(
                Map.entry("total_cost", 0),
                Map.entry("person_id", 2)
            )
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .executeBatch(batchInsertParameters);

        List<Cart> carts = cartDao.findAll();

        assertThat(carts).isEqualTo(
            List.of(
                Cart.of(0).withId(1),
                Cart.of(0).withId(2)
            )
        );
    }

    @Test
    @DisplayName("Cart not deleted in case when not exists")
    void cart_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> cartDao.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cart deleted")
    void cart_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("person_id", 1)
                )
            );

        var cartsCountBeforeDeletion = fetchCartsCount();

        assertThat(cartsCountBeforeDeletion).isEqualTo(1);

        cartDao.delete(1);

        var cartsCount = fetchCartsCount();

        assertThat(cartsCount).isZero();
    }

    @Test
    @DisplayName("Cart updated")
    void cart_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("cart")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(
                Map.ofEntries(
                    Map.entry("total_cost", 0),
                    Map.entry("person_id", 1)
                )
            );

        cartDao.update(1, 100);

        var updatedCart = jdbcTemplate.queryForObject(
            "SELECT total_cost FROM cart WHERE id=:id",
            Map.ofEntries(Map.entry("id", 1)),
            Double.class
        );

        assertThat(updatedCart).isEqualTo(100);
    }
}
