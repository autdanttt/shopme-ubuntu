package com.shopme.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "brands")
public class Brand extends IdBasedEntity{

    @Column(nullable = false, length = 45, unique = true)
    private String name;
    @Column(nullable = false, length = 128)
    private String logo;
    @ManyToMany
    @JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"),inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Brand() {
    }

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
    }

    public Brand(Integer id, String name){
        this.id = id;
        this.name = name;
    }
    public Brand(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public Brand(String name, String logo, Set<Category> categories) {
        this.name = name;
        this.logo = logo;
        this.categories = categories;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categories=" + categories +
                '}';
    }
    @Transient
    public String getLogoPath(){
        if(this.id == null) return "/images/image-thumbnail.png";
        return "/brand-logos/" + this.id + "/"+ this.logo;
    }
}
