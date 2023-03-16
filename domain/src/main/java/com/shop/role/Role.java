package com.shop.role;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
    private long id;
    private String name;

    public Role() {
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role of(String name) {
        return new Role(0, name);
    }

    public Role withId(long id) {
        return new Role(id, this.name);
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
        Role that = (Role) o;
        return Objects.equals(id, that.id)
            && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Role{"
            + "id=" + id
            + ", name='" + name + '\''
            + '}';
    }
}
