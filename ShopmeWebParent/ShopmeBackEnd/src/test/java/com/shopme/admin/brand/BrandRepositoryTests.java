package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {
    @Autowired
    private BrandRepository repo;
    @Test
    public void testCreateBrand1(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");
        acer.getCategories().add(laptops);
        acer.setLogo("logo-default.png");
        Brand savedBrand = repo.save(acer);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Category cellphones = new Category(4);
        Category tablets = new Category(7);
        Brand apple = new Brand("Apple");
        apple.setLogo("logo-default.png");
        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);

        Brand savedBrand = repo.save(apple);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateBrand3(){
        Brand samsung = new Brand("Samsung");
        samsung.getCategories().add(new Category(29));
        samsung.getCategories().add(new Category(24));
        samsung.setLogo("logo-default.png");
        Brand savedBrand = repo.save(samsung);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll(){
        Iterable<Brand> brands = repo.findAll();
        brands.forEach(brand -> System.out.println(brand.getName()));
        assertThat(brands).isNotEmpty();
    }
    @Test
    public void testGetById(){
        Brand brand = repo.findById(1).get();
        assertThat(brand.getName()).isEqualTo("Apple");
    }
    @Test
    public void testUpdateName(){
        String newName = "Samsung Electronics";
        Brand samsung = repo.findById(3).get();
        samsung.setName(newName);
        Brand savedBrand = repo.save(samsung);
        assertThat(savedBrand.getName()).isEqualTo(newName);
    }
    @Test
    public void testDelete(){
        Integer id = 2;
        repo.deleteById(id);
        Optional<Brand> result = repo.findById(id);
        assertThat(result.isEmpty());
    }
}
