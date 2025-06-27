package com.shopme.admin.shoppingrate;

import com.shopme.admin.shippingrate.ShippingRateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRateTests {

    @Autowired
    private ShippingRateRepository shippingRepo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void createShippingRate(){
        Country country = entityManager.find(Country.class, 3);
        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setRate(200);
        shippingRate.setDays(2);
        shippingRate.setCodSupported(true);
        shippingRate.setCountry(country);
        shippingRate.setState("Atlanta");
        ShippingRate savedShippingRate = shippingRepo.save(shippingRate);
        assertThat(savedShippingRate.getId()).isGreaterThan(0);
    }
    @Test
    public void find(){
        ShippingRate shippingRate = shippingRepo.findByCountryAndState(1, "Ohio");
        assertThat(shippingRate).isNotNull();
    }
    @Test
    public void updateCOD(){
        shippingRepo.updateCODSupport(1, false);
        ShippingRate shippingRate = shippingRepo.findById(1).get();
        assertThat(shippingRate.isCodSupported()).isFalse();
    }
    @Test
    public void findAllTest(){
        Pageable pageable = PageRequest.of(2,2);
        Page<ShippingRate> shippingRates = shippingRepo.findAll("O", pageable);

        assertThat(shippingRates.getTotalElements()).isGreaterThan(0);

    }


}
