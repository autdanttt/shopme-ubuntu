package com.shopme.setting;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatesRestController {

    @Autowired private StateRepository repo;

    @GetMapping("settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id")Integer countryId){
        List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result = new ArrayList<>();
        for (State state : listStates){
            result.add(new StateDTO(state.getId(), state.getName()));
        }
        return result;
    }
}
