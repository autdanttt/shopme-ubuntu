package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService {
    public static final int BRANDS_PER_PAGE = 5;
    @Autowired
    private BrandRepository repo;

    public List<Brand> listAll(){
        return (List<Brand>) repo.findAll();
    }
    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
    }
    public Brand save(Brand brand){
        return repo.save(brand);
    }
    public Brand get(Integer id) throws BrandNotFoundException{
        try{
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new BrandNotFoundException("Could not find any brand with ID"+ id);
        }
    }
    public void delete(Integer id) throws BrandNotFoundException{
        Long countByID = repo.countById(id);
        if(countByID == null || countByID == 0){
            throw new BrandNotFoundException("Could not found brand with ID "+ id);
        }
        repo.deleteById(id);
    }

    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id==null || id == 0);
        Brand brandByName = repo.findByName(name);
        if(isCreatingNew){
            if(brandByName != null){
                return "DuplicateName";
            }
        }else{
            if(brandByName != null && brandByName.getId() != id){
                return "DuplicateName";
            }
        }
        return "OK";
    }
}
