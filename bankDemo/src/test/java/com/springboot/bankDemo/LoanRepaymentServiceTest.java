package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.enums.AccountStatus;
import com.springboot.bankDemo.enums.LoanApplicationStatus;
import com.springboot.bankDemo.enums.LoanStatus;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.Loan;
import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.model.LoanRepayment;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.LoanRepaymentRepository;
import com.springboot.bankDemo.repository.LoanRepository;
import com.springboot.bankDemo.service.LoanRepaymentService;
import com.springboot.bankDemo.service.LoanService;
import com.springboot.bankDemo.service.TransactionService;

@SpringBootTest
public class LoanRepaymentServiceTest {

	@InjectMocks
	private LoanRepaymentService loanRepaymentService;
	@Mock
	private LoanRepaymentRepository loanRepaymentRepository;
	@Mock
	private LoanService loanService;
	@Mock
	private LoanRepository loanRepository;
	@Mock
    private TransactionService transactionService;

	private Loan loan;
	private LoanRepayment loanRepayment;

	@BeforeEach
	public void setup() {
		User user = new User();
        user.setId(1);
        user.setUsername("david@gmail.com");
        user.setPassword("david@123");
        user.setRole("CUSTOMER");

        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("David");
        customer.setLastName("Miller");
        customer.setEmail("david@gmail.com");
        customer.setPhoneNumber("9876543210");
        customer.setAddress("Mumbai");
        customer.setRegistrationDate(LocalDate.now());
        customer.setUser(user);

        Branch branch = new Branch();
        branch.setId(1);
        branch.setIfscCode("IFSC0003");
        branch.setBranchName("Coimbatore");
        branch.setAddress("Gandhipuram");
        branch.setEmail("cbe@bank.com");
        branch.setPhoneNumber("9876543211");

        AccountType accountType = new AccountType();
        accountType.setId(1);
        accountType.setType("SAVINGS");
        accountType.setInitialDeposit(new BigDecimal("5000.00"));

        Account account = new Account();
        account.setId(1);
        account.setCustomer(customer);
        account.setBranch(branch);
        account.setAccountType(accountType);
        account.setBalance(new BigDecimal("5000.00"));
        account.setOpenDate(LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);
        account.setPanNumber("ABCDE1234F");
        account.setAadharNumber("123456789012");

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(1);
        loanApplication.setAccount(account);
        loanApplication.setApplicationDate(LocalDate.now());
        loanApplication.setStatus(LoanApplicationStatus.APPROVED);

        loan = new Loan();
        loan.setId(1);
        loan.setBalanceAmount(new BigDecimal("100000"));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setStartDate(LocalDate.of(2025, 1, 1));
        loan.setEndDate(LocalDate.of(2026, 1, 1));
        loan.setLoanApplication(loanApplication);

        loanRepayment = new LoanRepayment();
        loanRepayment.setId(1);
        loanRepayment.setLoan(loan);
        loanRepayment.setRepaymentAmount(new BigDecimal("20000"));
        loanRepayment.setRepaymentDate(LocalDate.now());
	}

	@Test
	public void postLoanRepaymentTest() {
		when(loanRepository.findById(1)).thenReturn(Optional.of(loan));
		when(transactionService.postLoanWithdraw(1, new BigDecimal("20000"))).thenReturn(null);	
		when(loanService.putLoanBalance(1, new BigDecimal("20000"))).thenReturn(loan);
		when(loanRepaymentRepository.save(any(LoanRepayment.class))).thenReturn(loanRepayment);
		// actual
		assertEquals(loanRepayment, loanRepaymentService.postLoanRepayment(1, new BigDecimal("20000")));
		
		// use case more than loan balance
		RuntimeException e = assertThrows(RuntimeException.class,
				() -> loanRepaymentService.postLoanRepayment(1, BigDecimal.valueOf(200000)));
		assertEquals("You cannot pay more than the loan balance amount", e.getMessage());
	}

	@Test
	public void getByLoanIdTest() {
		when(loanRepaymentRepository.getByLoanId(1)).thenReturn(List.of(loanRepayment));
		assertEquals(List.of(loanRepayment), loanRepaymentService.getByLoanId(1));
	}
}
