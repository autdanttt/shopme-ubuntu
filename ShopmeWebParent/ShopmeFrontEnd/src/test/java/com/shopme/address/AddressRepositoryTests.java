package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTests {

    @Autowired private AddressRepository repo;

    @Test
    public void testAddNew(){
        Integer customerId = 12;
        Integer countryId = 4;
        Address address = new Address();
        address.setCustomer(new Customer(customerId));
        address.setCountry(new Country(countryId));
        address.setFirstName("A");
        address.setLastName("Train");
        address.setPhoneNumber("911-119-9119");
        address.setAddressLine1("Vought Building");
        address.setCity("Wasington DC");
        address.setState("NSYK");
        address.setPostalCode("19923");

        Address savedAddress = repo.save(address);
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }
    @Test
    public void testFindByCustomer(){
        Integer customerId = 1;
        List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
        assertThat(listAddresses.size()).isGreaterThan(0);
        listAddresses.forEach(System.out::println);
    }
    @Test
    public void testFindByIdAndCustomer(){
        Integer addressId = 1;
        Integer customerId = 1;

        Address address = repo.findByIdAndCustomer(addressId, customerId);
        assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate(){
        Integer addressId = 2;
        String phoneNumber = "646-232-3932";

        Address address  = repo.findById(addressId).get();
       // address.setPhoneNumber(phoneNumber);
        address.setDefaultForShipping(true);
        Address updateAddress = repo.save(address);
//
    }


    @Test
    public void testDeleteByIdAndCustomer(){
        Integer addressId = 1;
        Integer customerId = 1;

        repo.deleteByIdAndCustomer(addressId, customerId);
        Address address = repo.findByIdAndCustomer(addressId, customerId);
        assertThat(address).isNull();
    }
    @Test
    public void testSetDefault(){
        Integer addressId = 7;
        repo.setDefaultAddress(addressId);

        Address address =  repo.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();

    }

    @Test
    public void testSetNonDefaultAddresses(){
        Integer addressId = 7;
        Integer customerId = 12;
        repo.setNonDefaultForOthers(addressId, customerId);
    }
    @Test
    public void testGetDefault(){
        Integer customerId = 12;
        Address address = repo.findDefaultByCustomer(customerId);
        assertThat(address).isNotNull();
        System.out.println(address);
    }

}
