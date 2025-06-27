package com.shopme.admin.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testCreatProduct(){
        Brand brand = entityManager.find(Brand.class, 2);
        Category category = entityManager.find(Category.class, 3);

        Product product  = new Product();
        product.setName("Acer Aspire 2000");
        product.setAlias("acer_aspire_2000");
        product.setShortDescription("A good laptop from Acer");
        product.setFullDescription("This is a very good Acer full description");
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(339);
        product.setCost(400);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());
        Product savedProduct = repo.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }
    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProducts = repo.findAll();
        iterableProducts.forEach(System.out::println);
    }
    @Test
    public void testGetProduct(){
        Integer id = 2;
        Product product = repo.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }
    @Test
    public void testUpdateProduct(){
        Integer id = 1;
        Product product  = repo.findById(id).get();
        product.setPrice(599);
        repo.save(product);
        Product updatedProduct = entityManager.find(Product.class, id);
        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }
    @Test
    public void testDeleteProduct(){
        Integer id = 1;
        repo.deleteById(id);
        Optional<Product> result = repo.findById(id);
        assertThat(!result.isPresent());
    }
    @Test
    public void testSaveProductWithImages(){
        Integer productId = 2;
        Product product = repo.findById(productId).get();
        product.setMainImage("main_image.png");
        product.addExtraImage("extra_image_1.png");
        product.addExtraImage("extra_image 2.png");

        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(2);
    }
    @Test
    public void testSaveProductWithDetails(){
        Integer productId = 2;
        Product product = repo.findById(productId).get();
        product.addDetail("Device Memory", "128GB");
        product.addDetail("CPU Model", "MediaTek");
        product.addDetail("OS", "Android 10");

        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();

    }
}
