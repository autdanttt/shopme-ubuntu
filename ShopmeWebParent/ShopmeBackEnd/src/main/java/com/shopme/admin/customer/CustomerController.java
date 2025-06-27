package com.shopme.admin.customer;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;


    @GetMapping("/customers")
    public String listFirstPage(){
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers")PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum){
        service.listByPage(pageNum, helper);
        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes ra){
        service.updateCustomerStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The customer ID " + id + " has been " + status;
        ra.addFlashAttribute("message", message);
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra){
        try{
            List<Country> countries = service.listAllCountries();

            Customer customer = service.get(id);
            model.addAttribute("listCountries", countries);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Edit Customer (ID: "+ id + ")");
            return "customers/customer_form";
        }catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }
    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes ra){
        service.save(customer);
        ra.addFlashAttribute("message", "The customer ID "+ customer.getId() + " has been updated successfully");
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes ra){
        try{
            service.delete(id);
            ra.addFlashAttribute("message", "The customer ID "+ id + " has been deleted successfully");
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomerDetail(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra){
        try{
            Customer customer = service.get(id);
            model.addAttribute("customer", customer);
            return "customers/customer_detail";
        }catch (CustomerNotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }



}
