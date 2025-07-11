package com.shopme.admin.product;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(Model model){
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }
    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products")PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") Integer pageNum,Model model,@Param("categoryId") Integer categoryId){
        productService.listByPage(pageNum, helper,categoryId);
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        if(categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("listCategories",listCategories);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        List<Brand> listBrands = brandService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);
        return "products/product_form";
    }
    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes ra,
                              @RequestParam(value = "fileImage", required = false)MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIDs", required = false) String[] detailsIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailsValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false)String[] imageNames,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser)
            throws IOException {
        if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if(loggedUser.hasRole("Salesperson")){
            productService.saveProductPrice(product);
            ra.addFlashAttribute("message", "The product has been saved successfully.");
            return "redirect:/products";
            }
        }
        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImageName(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
        ProductSaveHelper.setProductDetails(detailsIDs,detailNames, detailsValues,product);
        Product savedProduct = productService.save(product);
        ProductSaveHelper.savedUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
        ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);

        ra.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/products";
    }



    @GetMapping("/products/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes ra){
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The product ID "+ id + " has been "+ status;
        ra.addFlashAttribute("message", message);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra){
        try{
            productService.delete(id);
            String productExtraImagesDir = "../product-images/"+ id + "/extras";
            String productImagesDir = "../product-images/"+ id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);
            ra.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/products";
    }
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
                                @AuthenticationPrincipal ShopmeUserDetails loggedUser){
        try{
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();
            boolean isReadOnlyForSalesperson = false;
            if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if(loggedUser.hasRole("Salesperson")){
                    isReadOnlyForSalesperson = true;
                }
            }
            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Edit Product (ID: "+ id+ ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            return "products/product_form";
        }catch (ProductNotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try{
            Product product = productService.get(id);
            model.addAttribute("product", product);

            return "products/product_detail_modal";
        }catch (ProductNotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }
}
