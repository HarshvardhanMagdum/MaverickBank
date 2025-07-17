package com.springboot.bankDemo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.springboot.bankDemo.dto.AccountStatementDto;
import com.springboot.bankDemo.dto.StatementListDto;
import com.springboot.bankDemo.dto.TransactionDto;
import com.springboot.bankDemo.dto.TransactionListDto;
import com.springboot.bankDemo.enums.EntryType;
import com.springboot.bankDemo.enums.TransferType;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.Beneficiary;
import com.springboot.bankDemo.model.Transaction;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BeneficiaryRepository;
import com.springboot.bankDemo.repository.TransactionRepository;

@Service
public class TransactionService {

	Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
	public TransactionRepository transactionRepository;
	public AccountRepository accountRepository;
	public BeneficiaryRepository beneficiaryRepository;

	private Transaction transaction;

	public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
			BeneficiaryRepository beneficiaryRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
		this.beneficiaryRepository = beneficiaryRepository;
	}

	// deposit
	public Transaction postDeposit(int accountId, TransactionDto transactionDto) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		logger.info("Account details: "+account);
		BigDecimal amount = transactionDto.getAmount();
		logger.info("Amount need to be Deposite: "+amount);
		logger.info("Check amount is not null and not negative or zero");
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.error("Amount cannot be less than or equal to Zero");
			throw new RuntimeException("Amount cannot be less than or equal to Zero");
		}
		account.setBalance(account.getBalance().add(amount));
		logger.info("Save the amount to the account");
		accountRepository.save(account);
		logger.info("call function to set transaction datas");
		transaction = returnSetTransaction("DEPOSIT", amount, account, EntryType.CREDIT,
				transactionDto.getDescription(), account);
		logger.info("Save the transaction");
		return transactionRepository.save(transaction);
	}

	// withdraw
	public Transaction postWithdraw(int accountId, TransactionDto transactionDto) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		BigDecimal amount = transactionDto.getAmount();
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
			throw new RuntimeException("Amount cannot be less than or equal to Zero");
		if (account.getBalance().compareTo(amount) < 0)
			throw new RuntimeException("Insufficient balance in the account");
		account.setBalance(account.getBalance().subtract(amount));
		accountRepository.save(account);
		transaction = returnSetTransaction("WITHDRAW", amount, account, EntryType.DEBIT,
				transactionDto.getDescription(), account);
		return transactionRepository.save(transaction);
	}
	
	// loan deposite
	public Transaction postLoanDeposit(int accountId, BigDecimal amount) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
			throw new RuntimeException("Amount cannot be less than or equal to Zero");
		account.setBalance(account.getBalance().add(amount));
		accountRepository.save(account);
		String description = "Loan Amount"; 
		transaction = returnSetTransaction("LOAN", amount, account, EntryType.DEBIT, description, account);
		return transactionRepository.save(transaction);
	}
	
	// loan withdraw
	public Transaction postLoanWithdraw(int accountId, BigDecimal amount) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
			throw new RuntimeException("Amount cannot be less than or equal to Zero");
		if (account.getBalance().compareTo(amount) < 0)
			throw new RuntimeException("Insufficient balance in the account");
		account.setBalance(account.getBalance().subtract(amount));
		accountRepository.save(account);
		String description = "Loan Repayment"; 
		transaction = returnSetTransaction("LOAN", amount, account, EntryType.DEBIT, description, account);
		return transactionRepository.save(transaction);
	}

	// transfer
	public Transaction postTransfer(int accountId, int beneficiaryId, String transferType, TransactionDto transactionDto) {
		Account fromAccount = accountRepository.findById(accountId) // fetch fromAccount
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		BigDecimal amount = transactionDto.getAmount();
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
			throw new RuntimeException("Amount cannot be less than or equal to Zero");
		TransferType type = TransferType.valueOf(transferType); // fetch transfer type from enum
		BigDecimal charge = type.getCharge(); // fetch charge for transfer type
		BigDecimal totalDebit = amount.add(charge);
		if (fromAccount.getBalance().compareTo(totalDebit) < 0)
			throw new RuntimeException("Insufficient balance in the account");
		Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId) // fetch beneficiary
				.orElseThrow(() -> new RuntimeException("Beneficiary ID is Invalid"));
		Account toAccount = accountRepository.findById(beneficiary.getAccountNumber()) // fetch toAccount
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		// from-account and from-transaction setup
		fromAccount.setBalance(fromAccount.getBalance().subtract( // subtract amount+charge in fromAccount
				totalDebit));
		accountRepository.save(fromAccount); // save fromAccount
		Transaction fromTransaction = returnSetTransaction(transferType, totalDebit, toAccount, EntryType.DEBIT,
				transactionDto.getDescription(), fromAccount);
		transactionRepository.save(fromTransaction);
		// to-account and to-transaction setup
		toAccount.setBalance(toAccount.getBalance().add(amount)); // add amount in toAccount
		accountRepository.save(toAccount); // save toAccount
		Transaction toTransaction = returnSetTransaction(transferType, amount, fromAccount, EntryType.CREDIT,
				transactionDto.getDescription(), toAccount);
		return transactionRepository.save(toTransaction);
	}

	// fetch transaction by accountId between given date
	public List<TransactionListDto> getTxnBtwDateByAccId(int accountId, LocalDate fromDate, LocalDate tillDate) {
		List<Transaction> list = transactionRepository.getTxnBtwDateByAccId(accountId, fromDate, tillDate);
		return returnTransactionListDto(list);
	}

	// fetch transaction by accountId from given date
	public List<TransactionListDto> getTxnFromDateByAccId(int accountId, LocalDate fromDate) {
		List<Transaction> list = transactionRepository.getTxnFromDateByAccId(accountId, fromDate);
		return returnTransactionListDto(list);
	}

	// fetch last 10 transaction
	public List<TransactionListDto> getLast10TxnByAccId(int accountId) {
		List<Transaction> list = transactionRepository.getLast10TxnByAccId(accountId, PageRequest.of(0, 10));
		return returnTransactionListDto(list);
	}

	// fetch account statement
	public AccountStatementDto getAccStmtBtwDatebyAccId(int accountId, LocalDate fromDate, LocalDate toDate) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("ID is Invalid"));
		List<Transaction> list = transactionRepository.getTxnBtwDateByAccId(accountId, fromDate, toDate);
		List<StatementListDto> stmtList = returnStatementListDto(list);
		BigDecimal openingBalance = list.isEmpty() ? BigDecimal.ZERO : list.get(0).getBalanceAfterTxn();
		BigDecimal closingBalance = list.isEmpty() ? BigDecimal.ZERO : list.get(list.size() - 1).getBalanceAfterTxn();
		return new AccountStatementDto(account.getId(), account.getCustomer().getFirstName(), fromDate, toDate,
				openingBalance, closingBalance, stmtList);
	}

	
	// fetch transaction btw date by branch id
	public List<Transaction> getTxnBtwDateByBranchId(int branchId, LocalDate fromDate, LocalDate tillDate) {
		return transactionRepository.getTxnBtwDateByBranchId(branchId, fromDate, tillDate);
	}
	
	//fetch transaction from date by branch id
	public List<Transaction> getTxnFromDateByBranchId(int branchId, LocalDate fromDate) {
		return transactionRepository.getTxnFromDateByBranchId(branchId, fromDate);
	}
	
	// fetch transaction between given date
	public List<Transaction> getTransactionBetweenDate(LocalDate fromDate, LocalDate tillDate) {
		return transactionRepository.getTransactionBetweenDate(fromDate, tillDate);
	}

	// fetch transaction from a given date to till now
	public List<Transaction> getTransactionsFromDate(LocalDate fromDate) {
		return transactionRepository.getTransactionFromDate(fromDate);
	}

	// fetch all
	public List<Transaction> getAll() {
		return transactionRepository.findAll();
	}

	// return setTransaction
	public Transaction returnSetTransaction(String txnType, BigDecimal amount, Account transferAccount, EntryType type,
			String description, Account account) {
		transaction = new Transaction();
		transaction.setTransactionType(txnType);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setAmount(amount);
		transaction.setTransferAccountId(transferAccount.getId());
		transaction.setEntryType(type);
		if (description != null)
			transaction.setDescription(description);
		transaction.setBalanceAfterTxn(account.getBalance());
		transaction.setAccount(account);
		return transaction;
	}

	// return for TransactionListDto
	public List<TransactionListDto> returnTransactionListDto(List<Transaction> list) {
		return list.stream().map(t -> {
			TransactionListDto dto = new TransactionListDto();
			dto.setTransactionType(t.getTransactionType() );
			dto.setTransactionDate(t.getTransactionDate());
			dto.setAmount(t.getAmount());
			dto.setEntryType(t.getEntryType());
			dto.setTransferAccountId(t.getTransferAccountId());
			dto.setDescription(t.getDescription());
			dto.setBalanceAfterTxn(t.getBalanceAfterTxn());
			return dto;
		}).toList();
	}

	// return for AccountStatementDto
	public List<StatementListDto> returnStatementListDto(List<Transaction> list) {
		return list.stream().map(t -> {
			StatementListDto dto = new StatementListDto();
			dto.setTransactionType(t.getTransactionType());
			dto.setTransactionDate(t.getTransactionDate());
			dto.setAmount(t.getAmount());
			dto.setEntryType(t.getEntryType());
			dto.setDescription(t.getDescription());
			dto.setBalanceAfterTxn(t.getBalanceAfterTxn());
			dto.setTransferAccountId(t.getTransferAccountId());
			return dto;
		}).toList();
	}
}
