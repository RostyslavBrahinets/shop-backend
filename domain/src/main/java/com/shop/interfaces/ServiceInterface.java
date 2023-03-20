package com.shop.interfaces;

import java.util.List;

public interface ServiceInterface<T> {
    List<T> findAll();

    T findById(long id);

    T save(T object);

    void delete(T object);
}
