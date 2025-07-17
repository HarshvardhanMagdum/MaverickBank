package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.enums.AccountStatus;
import com.springboot.bankDemo.exception.ResourceNotFoundException;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Beneficiary;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BeneficiaryRepository;
import com.springboot.bankDemo.repository.BranchRepository;
import com.springboot.bankDemo.repository.CustomerRepository;
import com.springboot.bankDemo.service.BeneficiaryService;

@SpringBootTest
public class BeneficiaryServiceTest {

	@InjectMocks
	private BeneficiaryService beneficiaryService;

	@Mock
	private BeneficiaryRepository beneficiaryRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private BranchRepository branchRepository;

	private Customer customer;
	private Beneficiary beneficiary;
	private Branch branch;
	private User user;
	private AccountType accountType;
	private Account account;

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
		customer.setAddress("Mumbai");
		customer.setRegistrationDate(LocalDate.now());
		customer.setUser(user);

		branch = new Branch();
		branch.setId(1);
		branch.setIfscCode("IFSC0003");
		branch.setBranchName("Coimbatore");
		branch.setAddress("Gandhipuram");
		branch.setEmail("cbe@bank.com");
		branch.setPhoneNumber("9876543211");

		beneficiary = new Beneficiary();
		beneficiary.setId(1);
		beneficiary.setName("Alice");
		beneficiary.setAccountNumber(1);
		beneficiary.setIfscCode("IFSC0003");
		beneficiary.setBranchName("Coimbatore");
		beneficiary.setDescription("Friend");

		accountType = new AccountType();
		accountType.setId(1);
		accountType.setType("SAVINGS");
		accountType.setInitialDeposit(new BigDecimal("5000.00"));

		account = new Account();
		account.setId(1);
		account.setCustomer(customer);
		account.setBranch(branch);
		account.setAccountType(accountType);
		account.setBalance(accountType.getInitialDeposit());
		account.setOpenDate(LocalDate.now());
		account.setStatus(AccountStatus.ACTIVE);
		account.setPanNumber("ABCDE1234F");
		account.setAadharNumber("123456789012");
	}

	@Test
	public void postBeneficiaryTest() {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(branchRepository.getByIfscCode("IFSC0003")).thenReturn(Optional.of(branch));
		when(branchRepository.getBranchByNameIfsc("IFSC0003", "Coimbatore")).thenReturn(Optional.of(branch));
		when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);
		// actual
		assertEquals(beneficiary, beneficiaryService.postBeneficiary("david@gmail.com", beneficiary));

		// use case account is not active
		account.setStatus(AccountStatus.CLOSED);
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		RuntimeException e = assertThrows(RuntimeException.class,
				() -> beneficiaryService.postBeneficiary("david@gmail.com", beneficiary));
		assertEquals("Account is not active", e.getMessage());
	}

	@Test
	public void postBeneficiary_InvalidAccountId() {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(accountRepository.findById(4)).thenReturn(Optional.empty());

		RuntimeException e = assertThrows(RuntimeException.class,
				() -> beneficiaryService.postBeneficiary("david@gmail.com", beneficiary));

		assertEquals("Invalid Account ID", e.getMessage());
	}

	@Test
	public void postBeneficiary_InvalidIfsc() {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(branchRepository.getByIfscCode("IFSC0003")).thenReturn(Optional.empty()); // simulate invalid IFSC

		RuntimeException e = assertThrows(RuntimeException.class,
				() -> beneficiaryService.postBeneficiary("david@gmail.com", beneficiary));

		assertEquals("Invalid IFSC Code", e.getMessage());
	}

	@Test
	public void postBeneficiary_IfscBranchMismatch() {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(branchRepository.getByIfscCode("IFSC0003")).thenReturn(Optional.of(branch));
		when(branchRepository.getBranchByNameIfsc("IFSC0003", "Coimbatore")).thenReturn(Optional.empty()); // simulate
																											// mismatch

		RuntimeException e = assertThrows(RuntimeException.class,
				() -> beneficiaryService.postBeneficiary("david@gmail.com", beneficiary));

		assertEquals("IFSC Code and Branch Name Mismatching", e.getMessage());
	}

	@Test
	public void putBeneficiaryTest() {
		Beneficiary updated = new Beneficiary();
		updated.setName("Bob");
		updated.setAccountNumber(987654);
		updated.setIfscCode("IFSC5678");
		updated.setBranchName("Mumbai");
		updated.setDescription("Colleague");

		when(beneficiaryRepository.findById(1)).thenReturn(Optional.of(beneficiary));
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

		Beneficiary result = beneficiaryService.putBeneficiary(1, 1, updated);
		assertEquals("Bob", result.getName());
		assertEquals(987654, result.getAccountNumber());
		assertEquals("IFSC5678", result.getIfscCode());
		assertEquals("Mumbai", result.getBranchName());
		assertEquals("Colleague", result.getDescription());
	}
	
	@Test
	public void putBeneficiary_InvalidCustomerId() {
	    Beneficiary updated = new Beneficiary();
	    when(beneficiaryRepository.findById(1)).thenReturn(Optional.of(beneficiary));
	    when(customerRepository.findById(3)).thenReturn(Optional.empty());

	    RuntimeException e = assertThrows(RuntimeException.class,
	        () -> beneficiaryService.putBeneficiary(1, 3, updated));
	    assertEquals("Customer ID is Invalid", e.getMessage());
	}

	@Test
	public void putBeneficiary_InvalidBeneficiaryId() {
	    Beneficiary updated = new Beneficiary();
	    when(beneficiaryRepository.findById(2)).thenReturn(Optional.empty());

	    RuntimeException e = assertThrows(RuntimeException.class,
	        () -> beneficiaryService.putBeneficiary(2, 1, updated));
	    assertEquals("ID is Invalid", e.getMessage());
	}

	@Test
	public void getByCustomerUsernameTest() throws Exception {
		when(customerRepository.getCustomerByUsername("david@gmail.com")).thenReturn(customer);
		when(beneficiaryRepository.getByCustomerId(1)).thenReturn(Optional.of(List.of(beneficiary)));
		// actual
		assertEquals(List.of(beneficiary), beneficiaryService.getByCustomerUsername("david@gmail.com"));

		// use case resource not found
		when(beneficiaryRepository.getByCustomerId(1)).thenReturn(Optional.empty());
		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
				() -> beneficiaryService.getByCustomerUsername("david@gmail.com"));
		assertEquals("Resource not found", e.getMessage());
	}

	@Test
	public void getByIdTest() {
		when(beneficiaryRepository.findById(1)).thenReturn(Optional.of(beneficiary));
		// actual
		assertEquals(beneficiary, beneficiaryService.getById(1));

		// use case id is invalid
		RuntimeException e = assertThrows(RuntimeException.class, () -> beneficiaryService.getById(4));
		assertEquals("ID is Invalid", e.getMessage());
	}

	@Test
	public void getAllTest() {
		when(beneficiaryRepository.findAll()).thenReturn(List.of(beneficiary));
		assertEquals(List.of(beneficiary), beneficiaryService.getAll());
	}

	@Test
	public void deleteByIdTest() {
		when(beneficiaryRepository.findById(1)).thenReturn(Optional.of(beneficiary));
		beneficiaryService.deleteById(1);
		verify(beneficiaryRepository, times(1)).deleteById(1);

		// use case id is invalid
		when(beneficiaryRepository.findById(1)).thenReturn(Optional.empty());
		RuntimeException e = assertThrows(RuntimeException.class, () -> beneficiaryService.deleteById(4));
		assertEquals("ID is Invalid", e.getMessage());
	}

	@AfterEach
	public void afterTest() {
		user = null;
		customer = null;
		branch = null;
		beneficiary = null;
	}
}
