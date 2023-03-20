package com.shop.interfaces;

import java.util.List;

public interface RepositoryInterface<T> {
    List<T> findAll();

    T findById(long id);

    T save(T object);

    T update(T object);

    void delete(T object);
}
