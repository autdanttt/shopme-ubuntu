package com.shopme.common.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="roles")
public class Role extends IdBasedEntity{

    @Column(length=40, nullable = false, unique = true)
    private String name;
    @Column(length = 150, nullable = false)
    private String description;


    public Role() {
    }

    public Role(Integer id){
        this.id = id;
    }
    public Role(String name){
        this.name = name;
    }
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}













