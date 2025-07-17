package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.springboot.bankDemo.dto.AccountStatementDto;
import com.springboot.bankDemo.dto.TransactionDto;
import com.springboot.bankDemo.dto.TransactionListDto;
import com.springboot.bankDemo.enums.AccountStatus;
import com.springboot.bankDemo.enums.EntryType;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Beneficiary;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.model.Transaction;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BeneficiaryRepository;
import com.springboot.bankDemo.repository.TransactionRepository;
import com.springboot.bankDemo.service.TransactionService;

@SpringBootTest
public class TransactionServiceTest {

	@InjectMocks
	private TransactionService transactionService;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private BeneficiaryRepository beneficiaryRepository;

	private Account account;
	private Customer customer;
	private User user;
	private Branch branch;
	private AccountType accountType;
	private TransactionDto transactionDto;
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

		transactionDto = new TransactionDto();
		transactionDto.setAmount(new BigDecimal("1000"));
		transactionDto.setDescription("Test Transaction");

		transaction = new Transaction();
		transaction.setTransactionType("DEPOSIT");
		transaction.setTransactionDate(LocalDate.now());
		transaction.setAmount(transactionDto.getAmount());
		transaction.setTransferAccountId(account.getId());
		transaction.setEntryType(EntryType.CREDIT);
		transaction.setDescription("Test Transaction");
		transaction.setBalanceAfterTxn(account.getBalance().add(transactionDto.getAmount()));
		transaction.setAccount(account);
	}

	@Test
	public void postDepositTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction result = transactionService.postDeposit(1, transactionDto);
        assertEquals(transaction.getAmount(), result.getAmount());
	}

	@Test
	public void postWithdrawTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
		account.setBalance(new BigDecimal("5000.00")); // sufficient balance
		transaction.setTransactionType("WITHDRAW");
		transaction.setEntryType(EntryType.DEBIT);
		transaction.setBalanceAfterTxn(account.getBalance().subtract(transactionDto.getAmount()));
		Transaction result = transactionService.postWithdraw(1, transactionDto);
		assertEquals(transaction.getTransactionType(), result.getTransactionType());
	}
	
	@Test
	public void postLoanDepositTest() {
	    when(accountRepository.findById(1)).thenReturn(Optional.of(account));
	    when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
	    account.setBalance(new BigDecimal("5000.00"));
	    transaction.setTransactionType("LOAN DEPOSIT");
	    transaction.setEntryType(EntryType.CREDIT);
	    transaction.setBalanceAfterTxn(account.getBalance().add(BigDecimal.valueOf(100000)));
	    Transaction result = transactionService.postLoanDeposit(1, BigDecimal.valueOf(100000));
	    assertEquals("LOAN DEPOSIT", result.getTransactionType());
	    assertEquals(EntryType.CREDIT, result.getEntryType());
	    assertEquals(account.getId(), result.getAccount().getId());
	}

	@Test
	public void postLoanWithdrawTest() {
		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
		account.setBalance(new BigDecimal("150000.00"));
		transaction.setTransactionType("LOAN");
		transaction.setEntryType(EntryType.DEBIT);
		transaction.setBalanceAfterTxn(account.getBalance().subtract(transactionDto.getAmount()));
		Transaction result = transactionService.postLoanWithdraw(1, BigDecimal.valueOf(100000));
		assertEquals("LOAN", result.getTransactionType());
	}

	@Test
	public void postTransferTest() {
		Account toAccount = new Account();
		toAccount.setId(2);
		toAccount.setBalance(new BigDecimal("2000.00"));
		toAccount.setCustomer(customer);
		toAccount.setBranch(branch);
		toAccount.setAccountType(accountType);

		Beneficiary beneficiary = new Beneficiary();
		beneficiary.setId(1);
		beneficiary.setAccountNumber(toAccount.getId());

		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(beneficiaryRepository.findById(1)).thenReturn(Optional.of(beneficiary));
		when(accountRepository.findById(toAccount.getId())).thenReturn(Optional.of(toAccount));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		account.setBalance(new BigDecimal("5000.00"));
		transactionDto.setAmount(new BigDecimal("1000"));
		Transaction result = transactionService.postTransfer(1, 1, "IMPS", transactionDto);
		assertNotNull(result);
	}

	@Test
	public void getTxnBtwDateByAccIdTest() {
		List<Transaction> txnList = List.of(transaction);
		when(transactionRepository.getTxnBtwDateByAccId(eq(1), any(), any())).thenReturn(txnList);
		List<TransactionListDto> result = transactionService.getTxnBtwDateByAccId(1, LocalDate.now().minusDays(5),
				LocalDate.now());
		assertEquals(1, result.size());
	}

	@Test
	public void getTxnFromDateByAccIdTest() {
		List<Transaction> txnList = List.of(transaction);
		when(transactionRepository.getTxnFromDateByAccId(eq(1), any())).thenReturn(txnList);
		List<TransactionListDto> result = transactionService.getTxnFromDateByAccId(1, LocalDate.now().minusDays(3));
		assertFalse(result.isEmpty());
	}

	@Test
	public void getLast10TxnByAccIdTest() {
		when(transactionRepository.getLast10TxnByAccId(eq(1), any())).thenReturn(List.of(transaction));
		List<TransactionListDto> result = transactionService.getLast10TxnByAccId(1);
		assertEquals(1, result.size());
	}

	@Test
    public void getAccStmtBtwDatebyAccIdTest() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(transactionRepository.getTxnBtwDateByAccId(1, LocalDate.now().minusDays(5), LocalDate.now()))
                .thenReturn(List.of(transaction));
        AccountStatementDto result = transactionService.getAccStmtBtwDatebyAccId(1, LocalDate.now().minusDays(5), LocalDate.now());
        assertEquals(1, result.getAccountId());
        assertEquals(LocalDate.now().minusDays(5), result.getFromDate());
        assertEquals(LocalDate.now(), result.getToDate());
    }

	@Test
	public void getTransactionBetweenDateTest() {
		when(transactionRepository.getTransactionBetweenDate(any(), any())).thenReturn(List.of(transaction));
		assertEquals(List.of(transaction), transactionService.getTransactionBetweenDate(LocalDate.now().minusDays(5), LocalDate.now()));
	}

	@Test
	public void getTransactionsFromDateTest() {
		when(transactionRepository.getTransactionFromDate(any())).thenReturn(List.of(transaction));
		assertEquals(List.of(transaction), transactionService.getTransactionsFromDate(LocalDate.now()));
	}

	@Test
	public void getAllTest() {
		when(transactionRepository.findAll()).thenReturn(List.of(transaction));
		assertEquals(List.of(transaction), transactionService.getAll());
	}
	
	@AfterEach
	public void afterTest() {
		user = null;
		account = null;
		branch = null;
		accountType = null;
		customer = null;
		transaction = null;
		transactionDto = null;
	}
}
