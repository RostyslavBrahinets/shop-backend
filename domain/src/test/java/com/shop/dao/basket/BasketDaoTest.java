package com.shop.dao.basket;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.BasketDao;
import com.shop.dao.PersonDao;
import com.shop.models.Basket;
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
    "classpath:db/migration/basket/V20220421161946__Create_table_basket.sql"
})
public class BasketDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private BasketDao basketDao;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(jdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        basketDao = new BasketDao(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(
            jdbcTemplate.getJdbcTemplate(),
            "basket"
        );
    }

    private int fetchBasketsCount() {
        return JdbcTestUtils.countRowsInTable(
            jdbcTemplate.getJdbcTemplate(),
            "basket"
        );
    }

    @Test
    @DisplayName("Save basket")
    void save_basket() {
        basketDao.save(0, 1);

        var basketsCount = fetchBasketsCount();

        assertThat(basketsCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Save multiple basket")
    void save_multiple_basket() {
        basketDao.save(0, 1);
        basketDao.save(0, 2);

        var basketsCount = fetchBasketsCount();

        assertThat(basketsCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Basket by id was not found")
    void basket_by_id_was_not_found() {
        Optional<Basket> basket = basketDao.findById(1);

        assertThat(basket).isEmpty();
    }

    @Test
    @DisplayName("Basket by id was found")
    void basket_by_id_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("basket")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(Map.of("total_cost", 0, "person_id", 1));

        Optional<Basket> basket = basketDao.findById(1);

        assertThat(basket).get().isEqualTo(Basket.of(0).withId(1));
    }

    @Test
    @DisplayName("Basket by person was not found")
    void basket_by_person_was_not_found() {
        Optional<Basket> basket = basketDao.findByPerson(1);

        assertThat(basket).isEmpty();
    }

    @Test
    @DisplayName("Basket by person was found")
    void basket_by_person_was_found() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("basket")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(Map.of("total_cost", 0, "person_id", 1));

        Optional<Basket> basket = basketDao.findByPerson(1);

        assertThat(basket).get().isEqualTo(Basket.of(0).withId(1));
    }

    @Test
    @DisplayName("Find all baskets")
    void find_all_baskets() {
        var batchInsertParameters = SqlParameterSourceUtils.createBatch(
            Map.of("total_cost", 0, "person_id", 1),
            Map.of("total_cost", 0, "person_id", 2)
        );

        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("basket")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .executeBatch(batchInsertParameters);

        List<Basket> baskets = basketDao.findAll();

        assertThat(baskets).isEqualTo(
            List.of(
                Basket.of(0).withId(1),
                Basket.of(0).withId(2)
            )
        );
    }

    @Test
    @DisplayName("Basket not deleted in case when not exists")
    void basket_not_deleted_in_case_when_not_exists() {
        assertThatCode(() -> basketDao.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Basket deleted")
    void basket_deleted() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("basket")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(Map.of("total_cost", 0, "person_id", 1));

        var basketsCountBeforeDeletion = fetchBasketsCount();

        assertThat(basketsCountBeforeDeletion).isEqualTo(1);

        basketDao.delete(1);

        var basketsCount = fetchBasketsCount();

        assertThat(basketsCount).isZero();
    }

    @Test
    @DisplayName("Basket updated")
    void basket_updated() {
        new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
            .withTableName("basket")
            .usingGeneratedKeyColumns("id")
            .usingColumns("total_cost", "person_id")
            .execute(Map.of("total_cost", 0, "person_id", 1));

        basketDao.update(1, 100);

        var updatedBasket = jdbcTemplate.queryForObject(
            "SELECT total_cost FROM basket WHERE id=:id",
            Map.of("id", 1),
            Double.class
        );

        assertThat(updatedBasket).isEqualTo(100);
    }
}
