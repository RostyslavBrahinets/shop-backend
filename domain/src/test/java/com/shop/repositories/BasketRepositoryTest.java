package com.shop.repositories;

import com.shop.configs.DatabaseConfig;
import com.shop.dao.BasketDao;
import com.shop.dao.PersonDao;
import com.shop.models.Basket;
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
    BasketRepositoryTest.TestContextConfig.class
})
@Sql(scripts = {
    "classpath:db/migration/person/V20220421161641__Create_table_person.sql",
    "classpath:db/migration/basket/V20220421161946__Create_table_basket.sql"
})
public class BasketRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private BasketDao basketDao;

    @Autowired
    private BasketRepository basketRepository;

    @BeforeEach
    void setUp() {
        PersonDao personDao = new PersonDao(namedParameterJdbcTemplate);

        String firstName = "First Name";
        String lastName = "Last Name";

        personDao.save(firstName, lastName);
        personDao.save(firstName, lastName);

        namedParameterJdbcTemplate.getJdbcTemplate().execute("TRUNCATE TABLE basket");
    }

    @Test
    @DisplayName("No baskets in database")
    void no_history_records_in_db() {
        int basketsCount = basketRepository.count();

        assertThat(basketsCount).isZero();
    }

    @Test
    @DisplayName("Nothing happened when trying to delete not existing basket")
    void nothing_happened_when_trying_to_delete_not_existing_basket() {
        assertThatCode(() -> basketRepository.delete(1))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Basket was deleted")
    @DirtiesContext
    void basket_was_deleted() {
        var basketToSave = Basket.of(0);

        basketRepository.save(basketToSave, 1);

        assertThat(basketRepository.count()).isEqualTo(1);

        basketRepository.delete(basketRepository.count());

        assertThat(basketRepository.count()).isZero();
    }

    @Test
    @DisplayName("Save basket and check note data")
    @DirtiesContext
    void save_note_and_check_note_data() {
        var basketToSave = Basket.of(0);
        basketRepository.save(basketToSave, 1);
        var savedBasket = basketRepository.findById(basketRepository.count());
        Basket basket = null;
        if (savedBasket.isPresent()) {
            basket = savedBasket.get();
        }

        assertThat(basket).extracting(Basket::getId).isEqualTo(1L);
        assertThat(basket).extracting(Basket::getTotalCost).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Save multiple baskets")
    @DirtiesContext
    void save_multiple_baskets() {
        basketRepository.save(Basket.of(0), 1);
        basketRepository.save(Basket.of(0), 2);

        assertThat(basketRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Basket was not found")
    void basket_was_not_found() {
        Optional<Basket> noteEntity = basketRepository.findById(1);

        assertThat(noteEntity).isEmpty();
    }

    @Test
    @DisplayName("Basket was found")
    void basket_was_found() {
        basketRepository.save(Basket.of(0), 1);

        Optional<Basket> basket = basketRepository.findById(basketRepository.count());

        assertThat(basket).get().isEqualTo(Basket.of(0).withId(1));
    }

    @Test
    @DisplayName("Find all baskets")
    @DirtiesContext
    void find_all_baskets() {
        basketRepository.save(Basket.of(0), 1);
        basketRepository.save(Basket.of(0), 2);

        var baskets = basketRepository.findAll();

        assertThat(baskets).isEqualTo(
            List.of(
                Basket.of(0).withId(1),
                Basket.of(0).withId(2)
            )
        );
    }

    @TestConfiguration
    static class TestContextConfig {
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        @Bean
        public BasketDao basketDao() {
            return new BasketDao(namedParameterJdbcTemplate);
        }

        @Bean
        public BasketRepository basketRepository(BasketDao basketDao) {
            return new BasketRepository(basketDao);
        }
    }
}
