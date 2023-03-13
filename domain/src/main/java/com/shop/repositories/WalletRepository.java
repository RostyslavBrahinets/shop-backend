package com.shop.repositories;

import com.shop.models.Wallet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WalletRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WalletRepository(NamedParameterJdbcTemplate jdbcTemplate) {
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

    public Optional<Wallet> findByUser(long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM wallet WHERE user_id=:user_id",
                Map.ofEntries(Map.entry("user_id", userId)),
                new BeanPropertyRowMapper<>(Wallet.class)
            )
            .stream().findAny();
    }

    public void save(
        String number,
        double amountOfMoney,
        long userId
    ) {
        jdbcTemplate.update(
            "INSERT INTO wallet (number, amount_of_money, user_id)"
                + " VALUES (:number, :amount_of_money, :user_id)",
            Map.ofEntries(
                Map.entry("number", number),
                Map.entry("amount_of_money", amountOfMoney),
                Map.entry("user_id", userId)
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
