package com.shopme.admin.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {
    @Autowired
    private StateRepository repo;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testCreateStatesInIndia(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

//        State state = repo.save(new State("Karnataka", country));
//        State state = repo.save(new State("Punjab", country));
        State state = repo.save(new State("Uttar", country));
        assertThat(state).isNotNull();

        assertThat(state.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateStatesInUs(){
        Integer countryId = 3;
        Country country = entityManager.find(Country.class, countryId);

//        State state = repo.save(new State("California", country));
//        State state = repo.save(new State("Texas", country));
       State state = repo.save(new State("New York", country));
   //     State state = repo.save(new State("Washington", country));
        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }
    @Test
    public void testListStateByCountry(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);
        List<State> listStates = repo.findByCountryOrderByNameAsc(country);
        listStates.forEach(System.out::println);
        assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState(){
        Integer stateId = 1;
        String statesName = "Tamil Nadu";
        State state = repo.findById(stateId).get();
        state.setName(statesName);
        State updatedStates = repo.save(state);
        assertThat(updatedStates.getName()).isEqualTo(statesName);
    }
    @Test
    public void testGetStates(){
        Integer stateId = 1;
        Optional<State> findById = repo.findById(stateId);
        assertThat(findById.isPresent());
    }

    @Test
    public void testDeleteState(){
        Integer stateId = 5;
        repo.deleteById(stateId);
        Optional<State> findById = repo.findById(stateId);
        assertThat(findById.isEmpty());
    }

}
