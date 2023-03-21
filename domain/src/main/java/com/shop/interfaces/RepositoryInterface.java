package com.shop.interfaces;

import java.util.List;
import java.util.Optional;

public interface RepositoryInterface<T> {
    List<T> findAll();

    Optional<T> findById(long id);

    void save(T object);

    void update(T object);

    void delete(T object);
}
