package com.shopme.address;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.SimpleTimeZone;

@Controller
public class AddressController {

    @Autowired private AddressService addressService;
    @Autowired private CustomerService customerService;
    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> listAddresses = addressService.listAddressBook(customer);

        boolean usePrimaryAddressAsDefault = true;
        for(Address address : listAddresses){
            if(address.isDefaultForShipping()){
                usePrimaryAddressAsDefault = false;
                break;
            }
        }
        model.addAttribute("listAddresses", listAddresses);
        model.addAttribute("customer", customer);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        return "address_book/addresses";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if(email == null){
            throw new CustomerNotFoundException("No authenticated customer");
        }
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/address_book/new")
    public String newAddress(Model model){
        List<Country> listCountries = customerService.listAllCountries();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("address", new Address());
        model.addAttribute("pageTitle", "Add New Address");

        return "address_book/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        address.setCustomer(customer);
        addressService.save(address);
        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/address_book";
        if("checkout".equals(redirectOption)){
            redirectURL += "?redirect=checkout";
        }

        ra.addFlashAttribute("message", "The address has been saved successfully");
        return redirectURL;
    }

    @GetMapping("/address_book/edit/{id}")
    public String editAddress(@PathVariable(name = "id") Integer addressId, Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Country> listCountries = customerService.listAllCountries();
        Address address = addressService.get(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Edit Address (ID: "+ addressId + ")");
        return "address_book/address_form";
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable(name = "id") Integer addressId, RedirectAttributes ra, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.delete(addressId, customer.getId());
        ra.addFlashAttribute("message", "The address with ID "+ addressId + "has been deleted");
        return "redirect:/address_book";
    }

    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.setDefaultAddress(addressId, customer.getId());

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/address_book";

        if("cart".equals(redirectOption)){
            redirectURL = "redirect:/cart";
        } else if ("checkout".equals(redirectOption)) {
            redirectURL = "redirect:/checkout";
        }
        return redirectURL;
    }
}
