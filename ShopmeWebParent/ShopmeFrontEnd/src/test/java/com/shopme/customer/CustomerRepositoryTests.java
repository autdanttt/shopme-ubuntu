package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repo;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testCreateCustomer1(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("David");
        customer.setLastName("Linus");
        customer.setPassword("password123");
        customer.setEmail("david.s.fountaine@gmail.com");
        customer.setPhoneNumber("301-200-2002");
        customer.setAddressLine1("1029 Wied file");
        customer.setCity("Sacramento");
        customer.setState("California");
        customer.setPostalCode("98232");
        customer.setCreateTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateCustomer2(){
        Integer countryId = 3;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Alex");
        customer.setLastName("Perera");
        customer.setPassword("password123");
        customer.setEmail("alex.s.fountaine@gmail.com");
        customer.setPhoneNumber("301-200-2002");
        customer.setAddressLine1("Mumbai");
        customer.setAddressLine2("Dhanraj Mill Compound");
        customer.setCity("Mumbai");
        customer.setState("Maharashtra");
        customer.setPostalCode("300392");
        customer.setCreateTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers(){
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);
        assertThat(customers).hasSizeGreaterThan(1);
    }
    @Test
    public void testUpdateCustomer(){
        Integer customerId = 1;
        String lastName = "Stanfield";
        Customer customer = repo.findById(customerId).get();
        customer.setVerificationCode("code_123");
        Customer updateCustomer = repo.save(customer);
        assertThat(updateCustomer.getLastName()).isEqualTo(lastName);
    }
    @Test
    public void testGetCustomer(){
        Integer customerId = 2;
        Optional<Customer> findById = repo.findById(customerId);

        assertThat(findById).isPresent();
        Customer customer = findById.get();
        System.out.println(customerId);
    }
    @Test
    public void testDeleteCustomer(){
        Integer customerId = 2;
        repo.deleteById(customerId);
        Optional<Customer> findById = repo.findById(customerId);
        assertThat(findById).isNotPresent();
    }
    @Test
    public void testFindByEmail(){
        String email = "david.s.fountaine@gmail.com";
        Customer customer = repo.findByEmail(email);
        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode(){
        String code = "code_123";
        Customer customer = repo.findByVerificationCode(code);
        assertThat(customer).isNotNull();
        System.out.println(customer);
    }
    @Test
    public void testEnabledCustomer(){
        Integer id = 1;
        repo.enable(id);
        Customer customer = repo.findById(id).get();
        assertThat(customer.isEnabled()).isTrue();
    }
    @Test
    public void testUpdateAuthenticationType(){
        Integer id = 1;
        repo.updateAuthenticationType(id, AuthenticationType.DATABASE);
        Customer customer = repo.findById(id).get();
        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
    }
}
