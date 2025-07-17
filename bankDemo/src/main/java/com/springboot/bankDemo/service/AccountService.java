package com.springboot.bankDemo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springboot.bankDemo.enums.AccountStatus;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BranchRepository;

@Service
public class AccountService {
	
	Logger logger = LoggerFactory.getLogger(AccountService.class);

	private AccountRepository accountRepository;
	private CustomerService customerService;
	private AccountTypeService accountTypeService;
	private BranchRepository branchRepository;

	public AccountService(AccountRepository accountRepository, CustomerService customerService,
			AccountTypeService accountTypeService, BranchRepository branchRepository) {
		this.accountRepository = accountRepository;
		this.customerService = customerService;
		this.accountTypeService = accountTypeService;
		this.branchRepository = branchRepository;
	}

	// insert by username - customer login cred
	public Account postAccountByUsername(String username, String ifscCode, String type, Account account) {
		Customer customer = customerService.getCustomerByUsername(username);
		logger.info("Customer info: "+customer);
		Branch branch = branchRepository.getByIfscCode(ifscCode).orElseThrow(() -> new RuntimeException("IFSC code is Invalid"));
		logger.info("Branch info: "+branch);
		AccountType accountType = accountTypeService.getByType(type);
		logger.info("AccountType info: "+accountType);
		boolean exists = accountRepository.getAccountExistsByCustomerandType(customer, accountType);
		logger.info("account exists fo this type already for the customer? "+exists);
		if(exists) {
			logger.error("Customer has already has an account of this type");
			throw new RuntimeException("Customer already has an account of this type");
		}
		logger.info("Customer not has an account of this type, So proceed account creation");
		account.setAccountType(accountType);
		account.setBalance(accountType.getInitialDeposit());
		account.setOpenDate(LocalDate.now());
		account.setStatus(AccountStatus.PENDING_APPROVAL);
		account.setBranch(branch);
		account.setCustomer(customer);
		logger.info("Save/create the account "+account);
		return accountRepository.save(account);
	}

	// create account by customer id
	public Account postAccountByCustomerId(int customerId, int branchId, String type, Account account) {
		Customer customer = customerService.getCustomerById(customerId);
		Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		AccountType accountType = accountTypeService.getByType(type);
		boolean exists = accountRepository.getAccountExistsByCustomerandType(customer, accountType);
		if(exists)
			throw new RuntimeException("Customer already has an account of this type");
		account.setCustomer(customer);
		account.setBranch(branch);
		account.setAccountType(accountType);
		account.setBalance(accountType.getInitialDeposit());
		account.setStatus(AccountStatus.ACTIVE);
		account.setOpenDate(LocalDate.now());
		return accountRepository.save(account);
	}
	
	// update account status
	public Account putAccountStatus(int accountId, String status) {
		logger.info("Get account info");
		Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		logger.info("Account details: "+account);
		logger.info("Account current status: "+account.getStatus());
		account.setStatus(AccountStatus.valueOf(status));
		logger.info("Account status after change: "+account.getStatus());
		logger.info("Save the account data");
		return accountRepository.save(account);
	}
	
	// update account balance
	public Account putAccountBalance(int accountId, BigDecimal amtToAdd) {
		Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		logger.info("Balance before update: "+account.getBalance());
		BigDecimal updatedBalance = account.getBalance();
		updatedBalance = updatedBalance.add(amtToAdd);
		account.setBalance(updatedBalance);
		logger.info("Balance after update: "+account.getBalance());
		return accountRepository.save(account);
	}
	
	// fetch account by id
	public Account getById(int accountId) {
		return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("ID is Invalid"));
	}
	
	// fetch account by customer id
	public List<Account> getByCustomerId(int customerId) {
		return accountRepository.getByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Customer Id has no Account"));
	}
	
	// fetch account by branch id 
	public List<Account> getByBranchId(int  branchId) {
		return accountRepository.getByBranchId(branchId).orElseThrow(() -> new RuntimeException("Branch Id is Invalid"));
	}
	
	
	// fetch account by username
	public List<Account> getByUsername(String username) {
		List<Account> list = accountRepository.getByUsername(username);
		if (list.isEmpty())
			throw new RuntimeException("Customer has no account");
		return list;
	}
	
	// fetch by status 
	public List<Account> getByStatus(String status) {
		return accountRepository.getByStatus(status).orElseThrow(() -> new RuntimeException("Status is Invalid"));
	}
	
	// fetch all account
	public List<Account> getAll() {
		return accountRepository.findAll();
	}

}
