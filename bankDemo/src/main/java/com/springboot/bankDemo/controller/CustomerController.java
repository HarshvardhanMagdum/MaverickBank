package com.springboot.bankDemo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.dto.CustomerRegisterDto;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins ="http://localhost:5173")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/*
	 * AIM: to insert values to customer with user info
	 * METHOD: POST 
	 * PARAM: CustomerRegisterDto -> RequestBody
	 * RESPONSE: Customer 
	 * PATH: /api/customer/post
	 */
	@PostMapping("/post")
	public ResponseEntity<Customer> postCustomer(@Valid @RequestBody CustomerRegisterDto dto) {
	    Customer customer = customerService.postCustomer(dto);
	    return ResponseEntity.status(HttpStatus.CREATED).body(customer);
	}
	/*
	 * AIM: fetch customer by loggedIn credentials
	 * METHOD: GET 
	 * PARAM: Principal
	 * RESPONSE: Customer 
	 * PATH: /api/customer/get
	 */
	@GetMapping("/get")
	public Customer getCustomerByUsername(Principal principal) {
		String username = principal.getName();
		return customerService.getCustomerByUsername(username);
	}
	
	/*
	 * AIM: fetch customer by id
	 * METHOD: GET 
	 * PARAM: CustomerID -> PathVariable
	 * RESPONSE: Customer 
	 * PATH: /api/customer/get-one/{customerId}
	 */
	@GetMapping("/get-one/{customerId}")
	public Customer getCustomerById(@PathVariable int customerId) {
		return customerService.getCustomerById(customerId);
	}

	/*
	 * AIM: fetch all customer 
	 * METHOD: GET 
	 * RESPONSE: List<Customer> 
	 * PATH:
	 * /api/customer/get-all
	 */
	@GetMapping("/get-all")
	public List<Customer> getAll() {
		return customerService.getAll();
	}
	/*
	 * AIM: update customer by loggedIn credential
	 * METHOD: PUT
	 * PARAM: CustomerId -> PathVariable, Customer -> RequestBody
	 * RESPONSE: Customer
	 * PATH: /api/customer/put
	 * */
	@PutMapping("/put")
	public Customer putCustomer(Principal principal, @RequestBody Customer updatedCustomer) {
		String username = principal.getName();
		return customerService.putCustomer(username, updatedCustomer);
	}
}
