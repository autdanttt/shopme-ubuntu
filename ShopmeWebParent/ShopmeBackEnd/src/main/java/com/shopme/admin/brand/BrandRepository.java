package com.shopme.admin.brand;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends SearchRepository<Brand, Integer> {
    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);
    public Long countById(Integer id);

    public Brand findByName(String name);

    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC ")
    public List<Brand> findAll();
}
