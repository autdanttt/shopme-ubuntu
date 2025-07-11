package com.shopme.admin.customer;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;
import org.hibernate.annotations.Formula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends SearchRepository<Customer, Integer> {

//    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, '', c.firstName, '', c.lastName, '', c.addressLine1, '', c.addressLine2, '', c.city, '', c.state, '', c.state, '', c.postalCode,'', c.country.name) LIKE %:keyword%")
    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, '', c.firstName, '', c.lastName, '', c.addressLine1) LIKE %:keyword%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);
    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnableStatus(Integer id, boolean enabled);

    public Long countById(Integer id);
}
