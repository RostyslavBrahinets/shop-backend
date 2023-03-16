package com.shop.category;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;
    private long id;
    private String name;

    public Category() {
    }

    public Category(
        long id,
        String name
    ) {
        this.id = id;
        this.name = name;
    }

    public static Category of(String name) {
        return new Category(0, name);
    }

    public Category withId(long id) {
        return new Category(id, this.name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category that = (Category) o;
        return Objects.equals(id, that.id)
            && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{"
            + "id=" + id
            + ", name='" + name + '\''
            + '}';
    }
}
