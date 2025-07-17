package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.springboot.bankDemo.enums.LoanApplicationStatus;
import com.springboot.bankDemo.enums.LoanStatus;
import com.springboot.bankDemo.enums.LoanType;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.Loan;
import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.model.LoanDetails;
import com.springboot.bankDemo.model.Transaction;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.LoanRepository;
import com.springboot.bankDemo.service.LoanService;
import com.springboot.bankDemo.service.TransactionService;

@SpringBootTest
public class LoanServiceTest {

	@InjectMocks
	private LoanService loanService;
	@Mock
	private LoanRepository loanRepository;
	@Mock
    private TransactionService transactionService;

	private User user;
    private Customer customer;
    private Branch branch;
    private AccountType accountType;
    private Account account;
    private LoanApplication loanApplication;
    private Loan loan;
    private LoanDetails loanDetails;
    private Transaction transaction;

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

		loanDetails = new LoanDetails();
        loanDetails.setId(1);
        loanDetails.setLoanType(LoanType.HOME);
        loanDetails.setPrincipalAmount(new BigDecimal("100000"));
        loanDetails.setInterestRate(new BigDecimal("0.1"));
        loanDetails.setTermInMonth(12);
        loanDetails.setTotalRepayableAmount(new BigDecimal("110000"));
        loanDetails.setEmiAmount(new BigDecimal("9166.67"));
		
        loanApplication = new LoanApplication();
        loanApplication.setId(1);
        loanApplication.setStatus(LoanApplicationStatus.APPROVED);
        loanApplication.setApplicationDate(LocalDate.now());
        loanApplication.setAccount(account);
        loanApplication.setLoanDetails(loanDetails);

        loan = new Loan();
        loan.setId(1);
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setBalanceAmount(new BigDecimal("100000"));
        loan.setStartDate(LocalDate.now());
        loan.setEndDate(LocalDate.now().plusMonths(12));
        loan.setLoanApplication(loanApplication);
        
        transaction = new Transaction();
	}

	@Test
	public void postLoanTest() {
		when(transactionService.postLoanDeposit(anyInt(), any(BigDecimal.class))).thenReturn(transaction);
		when(loanRepository.save(any(Loan.class))).thenReturn(loan);
		assertEquals(loan, loanService.postLoan(loanApplication));
	}

	@Test
	public void putLoanBalanceTest() {
		when(loanRepository.findById(1)).thenReturn(Optional.of(loan));
		when(loanRepository.save(any(Loan.class))).thenReturn(loan);
		assertEquals(loan, loanService.putLoanBalance(1, new BigDecimal("20000")));
	}

	@Test
	public void putLoanStatusTest() {
		when(loanRepository.findById(1)).thenReturn(Optional.of(loan));
		loan.setStatus(LoanStatus.CLOSED);
		when(loanRepository.save(loan)).thenReturn(loan);
		assertEquals(loan, loanService.putLoanStatus(1, "CLOSED"));
	}

	@Test
	public void getByBranchIdTest() {
		when(loanRepository.getByBranchId(1)).thenReturn(List.of(loan));
		assertEquals(List.of(loan), loanService.getByBranchId(1));
	}

	@Test
	public void getByIdTest() {
		when(loanRepository.findById(1)).thenReturn(Optional.of(loan));
		assertEquals(loan, loanService.getById(1));
	}

	@Test
	public void getAllTest() {
		when(loanRepository.findAll()).thenReturn(List.of(loan));
		assertEquals(List.of(loan), loanService.getAll());
	}
	
	@AfterEach
	public void aftrTest() {
		user = null;
		customer = null;
		branch = null;
		accountType = null;
		account = null;
		loanApplication = null;
		loan = null;
		loanDetails = null;
	}
}
