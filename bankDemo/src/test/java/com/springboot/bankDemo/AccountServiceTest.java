package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BranchRepository;
import com.springboot.bankDemo.service.AccountService;
import com.springboot.bankDemo.service.AccountTypeService;
import com.springboot.bankDemo.service.BranchService;
import com.springboot.bankDemo.service.CustomerService;

@SpringBootTest
public class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private CustomerService customerService;
	@Mock
	private AccountTypeService accountTypeService;
	@Mock
	private BranchRepository branchRepository;
	@Mock
	private BranchService branchService;

	private Customer customer;
	private Branch branch;
	private AccountType accountType;
	private Account account;
	private User user;

	@BeforeEach
	void setUp() {
		
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
		account.setStatus(AccountStatus.PENDING_APPROVAL);
		account.setPanNumber("ABCDE1234F");
		account.setAadharNumber("123456789012");
	}

	@Test
	void postAccountByUsernameTest() {
		when(customerService.getCustomerByUsername("user1")).thenReturn(customer);
		when(branchRepository.getByIfscCode("IFSC0003")).thenReturn(Optional.of(branch));
		when(accountTypeService.getByType("SAVINGS")).thenReturn(accountType);
		when(accountRepository.getAccountExistsByCustomerandType(customer, accountType)).thenReturn(false);
		when(accountRepository.save(any(Account.class))).thenReturn(account);
		//actual
		assertEquals(account, accountService.postAccountByUsername("user1", "IFSC0003", "SAVINGS", account));
	    
		// use case customer already has and account of the same type
		when(accountRepository.getAccountExistsByCustomerandType(customer, accountType)).thenReturn(true);
		RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.postAccountByUsername("user1", "IFSC0003", "SAVINGS", account);
	    });
	    assertEquals("Customer already has an account of this type".toLowerCase(), e.getMessage().toLowerCase());
	}

	@Test
    public void postAccountByCustomerIdTest() {
        when(customerService.getCustomerById(1)).thenReturn(customer);
        when(branchRepository.findById(1)).thenReturn(Optional.of(branch));
        when(accountTypeService.getByType("SAVINGS")).thenReturn(accountType);
        when(accountRepository.getAccountExistsByCustomerandType(customer, accountType)).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        // actual
        assertEquals(account, accountService.postAccountByCustomerId(1, 1, "SAVINGS", account));
        
        // use case customer already has and account of the same type
        when(accountRepository.getAccountExistsByCustomerandType(customer, accountType)).thenReturn(true);
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.postAccountByCustomerId(1, 1, "SAVINGS", account);
	    });
        assertEquals("Customer already has an account of this type".toLowerCase(), e.getMessage().toLowerCase());
    }
	
	@Test
	void putAccountStatusTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(accountRepository.save(account)).thenReturn(account);
		assertEquals(account, accountService.putAccountStatus(1, "ACTIVE"));
	}

	@Test
	void putAccountBalanceTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(accountRepository.save(account)).thenReturn(account);
		BigDecimal amtToAdd = new BigDecimal("1000.00");
		Account result = accountService.putAccountBalance(1, amtToAdd);
		assertEquals(new BigDecimal("6000.00"), result.getBalance());
	}

	@Test
	void getByIdTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		assertEquals(account, accountService.getById(1));
	}
	
	@Test
	public void getByCustomerIdTest() {
	    when(accountRepository.getByCustomerId(1)).thenReturn(Optional.of(List.of(account)));
	    // actual
	    assertEquals(List.of(account), accountService.getByCustomerId(1));
	    
	    // use case customer id has no account
	    RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.getByCustomerId(5);
	    });
	    assertEquals("Customer Id has no account".toLowerCase(), e.getMessage().toLowerCase());
	}
	
	@Test
	public void getByBranchIdTest() {
	    when(accountRepository.getByBranchId(1)).thenReturn(Optional.of(List.of(account)));
	    // actual
	    assertEquals(List.of(account), accountService.getByBranchId(1));
	    
	    // use case invalid branch id
	    RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.getByBranchId(5);
	    });
	    assertEquals("Branch Id is Invalid".toLowerCase(), e.getMessage().toLowerCase());
	}
	
	@Test
	public void getByUsernameTest() {
	    when(accountRepository.getByUsername("david@gmail.com")).thenReturn(List.of(account));
	    // actual
	    assertEquals(List.of(account), accountService.getByUsername("david@gmail.com"));
	    
	    // use case customer has no account
	    RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.getByUsername("dave@gmail.com");
	    });
	    assertEquals("Customer has no Account".toLowerCase(), e.getMessage().toLowerCase());
	}

	@Test
	public void getByStatusTest() {
	    when(accountRepository.getByStatus("ACTIVE")).thenReturn(Optional.of(List.of(account)));
	    // actual
	    assertEquals(List.of(account), accountService.getByStatus("ACTIVE"));
	    
	 // use case status is invalid
	    RuntimeException e = assertThrows(RuntimeException.class, () -> {
	        accountService.getByStatus("EVITCA");
	    });
	    assertEquals("status is Invalid".toLowerCase(), e.getMessage().toLowerCase());
	}


	@Test
	void getAllTest() {
		when(accountRepository.findAll()).thenReturn(List.of(account));
		assertEquals(List.of(account), accountService.getAll());
	}

	@AfterEach
	public void afterTest() {
		user = null;
		account = null;
		branch = null;
		accountType = null;
		customer = null;
	}
}
