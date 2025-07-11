package com.shopme.admin.customer;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 2;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);

    }

    public Customer get(Integer id) throws CustomerNotFoundException{
        try{
            return customerRepo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CustomerNotFoundException("Could not find any customer with ID "+ id);
        }
    }

    public boolean isEmailUnique(Integer id, String email){
        Customer existCustomer = customerRepo.findByEmail(email);
        if(existCustomer != null && existCustomer.getId() != id){
            return false;
        }
        return true;
    }
    public void save(Customer customerInForm){
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
        if(!customerInForm.getPassword().isEmpty()){
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        }else {
            customerInForm.setPassword(customerInDB.getPassword());
        }
        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreateTime(customerInDB.getCreateTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
        customerRepo.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long count = customerRepo.countById(id);
        if(count == null || count == 0){
            throw new CustomerNotFoundException("Could not find any customers with ID "+ id);
        }
        customerRepo.deleteById(id);
    }
    public void updateCustomerStatus(Integer id, boolean enabled){
        customerRepo.updateEnableStatus(id, enabled);
    }

    public List<Country> listAllCountries(){
        return countryRepo.findAllByOrderByNameAsc();
    }
}
