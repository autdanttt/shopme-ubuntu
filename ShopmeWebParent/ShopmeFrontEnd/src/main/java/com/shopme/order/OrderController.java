package com.shopme.order;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService  customerService;

    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
    }
    @GetMapping("/orders/page/{pageNum}")
    public String listOrdersByPage(Model model, HttpServletRequest request,
                                   @PathVariable("pageNum") int pageNum,
                                   String sortField, String sortDir, String orderKeyword) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);

        Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
        List<Order> listOrders = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("orderKeyword", orderKeyword);
        model.addAttribute("moduleURL", "/orders");
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + OrderService.ORDERS_PER_PAGE -1;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }
        model.addAttribute("endCount", endCount);

        return "orders/orders_customer";
    }


    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(Model model,
                                   @PathVariable(name = "id")Integer id,
                                   HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);

        Order order = orderService.getOrder(id, customer);
        model.addAttribute("order", order);
        return "orders/order_details_modal";
    }
    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if(email == null){
            throw new CustomerNotFoundException("No authenticated customer");
        }
        return customerService.getCustomerByEmail(email);
    }
}
