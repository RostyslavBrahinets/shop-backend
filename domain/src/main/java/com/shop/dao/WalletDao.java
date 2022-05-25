package com.shop.dao;

import com.shop.models.Wallet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class WalletDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WalletDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Wallet> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM wallet",
            new BeanPropertyRowMapper<>(Wallet.class)
        );
    }

    public Optional<Wallet> findById(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM wallet WHERE id=:id",
                Map.ofEntries(Map.entry("id", id)),
                new BeanPropertyRowMapper<>(Wallet.class)
            )
            .stream().findAny();
    }

    public Optional<Wallet> findByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM wallet WHERE person_id=:person_id",
                Map.ofEntries(Map.entry("person_id", personId)),
                new BeanPropertyRowMapper<>(Wallet.class)
            )
            .stream().findAny();
    }

    public void save(
        String number,
        double amountOfMoney,
        long personId
    ) {
        jdbcTemplate.update(
            "INSERT INTO wallet (number, amount_of_money, person_id)"
                + " VALUES (:number, :amount_of_money, :person_id)",
            Map.ofEntries(
                Map.entry("number", number),
                Map.entry("amount_of_money", amountOfMoney),
                Map.entry("person_id", personId)
            )
        );
    }

    public void update(long id, double amountOfMoney) {
        jdbcTemplate.update(
            "UPDATE wallet SET amount_of_money=:amount_of_money WHERE id=:id",
            Map.ofEntries(
                Map.entry("amount_of_money", amountOfMoney),
                Map.entry("id", id)
            )
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
            "DELETE FROM wallet WHERE id=:id",
            Map.ofEntries(Map.entry("id", id))
        );
    }
}
