package com.weshopify.platform.features.customers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weshopify.platform.features.customers.service.CustomerService;

@Controller
public class CustomerController {

	Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = { "/customer" }, method = RequestMethod.GET)
	public String customerSelfSignupPage() {
		log.info("in render signup page method");
		return "sign-up.html";
	}
	
	@RequestMapping(value = { "/customer-admin-reg" }, method = RequestMethod.GET)
	public String customerAdminSignupPage(Model model) {
		log.info("in customer-admin-reg");
		model.addAttribute("customer", new CustomerBean());
		return "customer-admin-reg.html";
	}
	
	@RequestMapping(value = { "/view-customers" }, method = RequestMethod.GET)
	public String viewCustomersPage(Model model) {
		log.info("in render signup page method");
		List<CustomerBean> customersList = customerService.findAllCustomers();
		model.addAttribute("customersList", customersList);
		return "customer.html";
	}
	
	@RequestMapping(value = { "/delete-customers/{customerId}" }, method = RequestMethod.GET)
	public String deleteCustomers(@PathVariable("customerId") int customerId, Model model) {
		
		log.info("deleteing the customet by the customer Id {}",customerId);
		
		List<CustomerBean> customersList = customerService.deleteCustomer(customerId);
		model.addAttribute("customersList", customersList);
		return "customer.html";
	}


	@RequestMapping(value = { "/customer" }, method = RequestMethod.POST)
	public String createCustomer(CustomerBean customer, Model model) {
		log.info("is Customer Self Registered:\t"+customer.isSelfReg());
		log.info(customer.toString());
		customer = customerService.saveCustomer(customer);
		if (customer.getCustomerId() > 0) {

			String message = "User Registration Successfull. Proceed with the Login Login";
			model.addAttribute("message", message);
		}
		if(customer.isSelfReg()) {
			return "sign-up.html";
		}else {
			return "redirect:/view-customers";
		}
		
	}
	
	@RequestMapping(value = { "/edit-customers/{customerId}" }, method = RequestMethod.GET)
	public String editCustomer(@PathVariable("customerId") int customerId, Model model) {
		log.info("customer ID in Edit Customer Page:\t"+customerId);
		CustomerBean customer = customerService.findCustomerById(customerId);
		model.addAttribute("customer",customer);
		return "customer-admin-reg.html";
		
	}
	
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String loginCustomer(CustomerBean customer, Model model) {
		log.info("Customer Registration information is:\t");
		log.info(customer.toString());
		String userName = customer.getUserName();
		String password = customer.getPassword();
		boolean isLogin = StringUtils.hasText(userName) && StringUtils.hasText(password);
		if (isLogin) {
			model.addAttribute("principal", userName);
			return "dashboard.html";
		}
		model.addAttribute("message", isLogin);
		
		return "home.html";
	}


}
