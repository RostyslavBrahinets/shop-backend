package com.shop.dao;

import com.shop.db.DatabaseTemplate;
import com.shop.models.Wallet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class WalletDao {
    private final NamedParameterJdbcTemplate jdbcTemplate = DatabaseTemplate.getJdbcTemplate();

    public List<Wallet> getWallets() {
        return jdbcTemplate.query(
            "SELECT * FROM wallet",
            new BeanPropertyRowMapper<>(Wallet.class)
        );
    }

    public void addWallet(Wallet wallet, long personId) {
        jdbcTemplate.update(
            "INSERT INTO wallet (number, amount_of_money, person_id)"
                + " VALUES (:number, :amount_of_money, :person_id)",
            Map.of(
                "number", wallet.getNumber(),
                "amount_of_money", wallet.getAmountOfMoney(),
                "person_id", personId
            )
        );
    }

    public void updateWallet(long id, Wallet updatedWallet) {
        jdbcTemplate.update(
            "UPDATE wallet SET amount_of_money=:amount_of_money WHERE id=:id",
            Map.of(
                "amount_of_money", updatedWallet.getAmountOfMoney(),
                "id", id
            )
        );
    }

    public void deleteWallet(long id) {
        jdbcTemplate.update(
            "DELETE FROM wallet WHERE id=:id",
            Map.of("id", id)
        );
    }

    public Optional<Wallet> getWallet(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM wallet WHERE id=:id",
                Map.of("id", id),
                new BeanPropertyRowMapper<>(Wallet.class)
            )
            .stream().findAny();
    }

    public Optional<Wallet> getWalletByPerson(long personId) {
        return jdbcTemplate.query(
                "SELECT * FROM wallet WHERE person_id=:person_id",
                Map.of("person_id", personId),
                new BeanPropertyRowMapper<>(Wallet.class)
            )
            .stream().findAny();
    }
}
