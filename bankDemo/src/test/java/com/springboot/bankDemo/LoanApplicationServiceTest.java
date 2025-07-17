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
import com.springboot.bankDemo.enums.LoanApplicationStatus;
import com.springboot.bankDemo.enums.LoanType;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.model.LoanDetails;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.LoanApplicationRepository;
import com.springboot.bankDemo.repository.LoanDetailsRepository;
import com.springboot.bankDemo.service.LoanApplicationService;
import com.springboot.bankDemo.service.LoanService;

@SpringBootTest
public class LoanApplicationServiceTest {

	@InjectMocks
    private LoanApplicationService loanApplicationService;
    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private LoanDetailsRepository loanDetailsRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private LoanService loanService;

    private User user;
    private Customer customer;
    private Branch branch;
    private AccountType accountType;
    private Account account;
    private LoanApplication loanApplication;
    private LoanDetails loanDetails;

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
        loanApplication.setStatus(LoanApplicationStatus.PENDING);
        loanApplication.setApplicationDate(LocalDate.now());
        loanApplication.setAccount(account);
        loanApplication.setLoanDetails(loanDetails);
    }

    @Test
    public void testPostLoanApplication() {
        when(loanDetailsRepository.findById(1)).thenReturn(Optional.of(loanDetails));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account)); 
        when(loanApplicationRepository.getLoanAppExistsByCustomerAndType(customer, LoanType.HOME)).thenReturn(false);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication saved = invocation.getArgument(0);
            saved.setId(1);
            return saved;
        });

        LoanApplication saved = loanApplicationService.postLoanApplication(1, 1);
        assertEquals(LoanApplicationStatus.PENDING, saved.getStatus());
    }
    
    @Test
    public void testPostLoanApplication_ThrowsIfAlreadyExists() {
        // Ensure both loanDetails and account are correctly mocked
        when(loanDetailsRepository.findById(1)).thenReturn(Optional.of(loanDetails));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        // Ensure account has the customer set
        account.setCustomer(customer);

        // Mock the repository call that triggers the null access
        when(loanApplicationRepository.getLoanAppExistsByCustomerAndType(customer, LoanType.HOME)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanApplicationService.postLoanApplication(1, 1);
        });

        assertEquals("Customer has already applied or has the Loan of this type", exception.getMessage());
    }


    @Test
    public void testPutLoanApplicationStatusCancelled() {
        when(loanApplicationRepository.findById(1)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationRepository.save(any())).thenReturn(loanApplication);

        LoanApplication result = loanApplicationService.putLoanApplicationStatusCancelled(1);
        assertEquals(LoanApplicationStatus.CANCELLED, result.getStatus());
    }

    @Test
    public void testGetByStatusAndUsername() {
        when(loanApplicationRepository.getByUsername("david@gmail.com"))
                .thenReturn(List.of(loanApplication));

        List<LoanApplication> result = loanApplicationService.getByUsername("david@gmail.com");
        assertEquals(1, result.size());
        assertEquals(LoanApplicationStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    public void testGetByStatus() {
        when(loanApplicationRepository.getByStatus("PENDING")).thenReturn(List.of(loanApplication));
        List<LoanApplication> result = loanApplicationService.getByStatus("PENDING");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetByBranchId() {
        when(loanApplicationRepository.getByBranchId("IFSC0003")).thenReturn(List.of(loanApplication));
        List<LoanApplication> result = loanApplicationService.getByBranchId("IFSC0003");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAll() {
        when(loanApplicationRepository.findAll()).thenReturn(List.of(loanApplication));
        List<LoanApplication> result = loanApplicationService.getAll();
        assertEquals(1, result.size());
    }

    @AfterEach
    public void afterTest() {
        user = null;
        customer = null;
        branch = null;
        accountType = null;
        account = null;
        loanApplication = null;
        loanDetails = null;
    }
}
