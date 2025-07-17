package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.dto.CustomerRegisterDto;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.CustomerRepository;
import com.springboot.bankDemo.service.CustomerService;
import com.springboot.bankDemo.service.UserService;

@SpringBootTest
public class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private UserService userService;

	private User user;
	private Customer customer;
	private CustomerRegisterDto customerRegisterDto;

	@BeforeEach
	public void init() {

		user = new User();
		user.setId(1);
		user.setUsername("david@gmail.com");
		user.setPassword("david@123");
		user.setRole("CUSTOMER");

		customer = new Customer();
		customer.setId(1);
		customer.setFirstName("David");
		customer.setLastName("Miller");
		customer.setEmail("david@gmail.com");
		customer.setPhoneNumber("9876543210");
		customer.setDateOfBirth(LocalDate.now());
		customer.setGender("MALE");
		customer.setAddress("Mumbai");
		customer.setRegistrationDate(LocalDate.now());
		customer.setUser(user);
		
		customerRegisterDto = new CustomerRegisterDto();
		customerRegisterDto.setFirstName("David");
		customerRegisterDto.setLastName("Miller");
		customerRegisterDto.setEmail("david@gmail.com");
		customerRegisterDto.setPhoneNumber("9876543210");
		customerRegisterDto.setDateOfBirth(LocalDate.now());
		customerRegisterDto.setGender("MALE");
		customerRegisterDto.setAddress("Mumbai");
		customerRegisterDto.setUsername("david@gmail.com");
		customerRegisterDto.setPassword("david@123");
	}

	@Test
	public void postCustomerTest() {
		when(userService.signUp(any(User.class))).thenReturn(user);
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		assertEquals(customer, customerService.postCustomer(customerRegisterDto));
	}

	@Test
	public void getCustomerByIdTest() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		// actual
		assertEquals(customer, customerService.getCustomerById(1));
		
		// use case id is invalid
		RuntimeException e = assertThrows(RuntimeException.class, () -> customerService.getCustomerById(2));
		assertEquals("ID is Invalid".toLowerCase(), e.getMessage().toLowerCase());
	}

	@Test
	public void getCustomerByUsernameTest() {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		assertEquals(customer, customerService.getCustomerByUsername("david@gmail.com"));
	}

	@Test
	public void getAllTest() {
		List<Customer> list = List.of(customer);
		when(customerRepository.findAll()).thenReturn(list);
		assertEquals(list, customerService.getAll());
	}
	
	@Test
	public void putCustomerTest() {
		Customer updated = new Customer();
		updated.setFirstName("David");
		updated.setLastName("Johnson");
		updated.setEmail("david.johnson@gmail.com");
		updated.setPhoneNumber("9999999999");
		updated.setAddress("Chennai");

		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Customer result = customerService.putCustomer("david@gmail.com", updated);
		assertEquals("Chennai", result.getAddress());
	}

	@AfterEach
	public void afterTest() {
		user = null;
		customer = null;
	}
}
