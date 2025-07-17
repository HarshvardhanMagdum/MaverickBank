package com.springboot.bankDemo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.dto.AccountStatementDto;
import com.springboot.bankDemo.dto.TransactionDto;
import com.springboot.bankDemo.dto.TransactionListDto;
import com.springboot.bankDemo.model.Transaction;
import com.springboot.bankDemo.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	/*
	 * AIM: post deposit transaction 
	 * PARAM: PathVariable -> accountId | RequestBody -> TransactionDto
	 * METHOD: POST
	 * RESPONSE: void
	 * PATH: /api/transaction/post/deposit/{accountId}
	 * */
	@PostMapping("/post/deposit/{accountId}")
	public Transaction postDeposite(@PathVariable int accountId, @RequestBody TransactionDto transactionDto) {
		return transactionService.postDeposit(accountId, transactionDto);
	}
	
	/*
	 * AIM: post withdraw transaction 
	 * PARAM: PathVariable -> accountId | RequestBody -> TransactionDto
	 * METHOD: POST
	 * RESPONSE: void
	 * PATH: /api/transaction/post/withdraw/{accountId}
	 * */
	@PostMapping("/post/withdraw/{accountId}")
	public Transaction postWithdraw(@PathVariable int accountId, @RequestBody TransactionDto transactionDto) {
		return transactionService.postWithdraw(accountId, transactionDto);
	}
	
	/*
	 * AIM: post transfer transaction 
	 * PARAM: PathVariable -> accountId, beneficiaryId | RequestParam -> transferType| RequestBody -> TransactionDto
	 * METHOD: POST
	 * RESPONSE: void
	 * PATH: /api/transaction/post/transfer/{accountId}/{beneficiaryId}?transferType=NEFT
	 * */
	@PostMapping("/post/transfer/{accountId}/{beneficiaryId}")
	public Transaction postTransfer(@PathVariable int accountId, @PathVariable int beneficiaryId, @RequestParam String transferType, @RequestBody TransactionDto transactionDto) {
		return transactionService.postTransfer(accountId, beneficiaryId, transferType, transactionDto);
	}
	
	/*
	 * AIM: get transaction by accountId between given dates
	 * PARAM: PathVariable -> accountId | RequestParam -> fromDate, tillDate, page, size
	 * METHOD: GET
	 * RESPONSE: List<TransactionListDto>
	 * PATH: /api/transaction/get-btw/{accountId}?fromDate=2025-06-03&tillDate=2025-06-06
	 * */
	@GetMapping("/get-btw/{accountId}")
	public List<TransactionListDto> getTxnBtwDateByAccId(@PathVariable int accountId,@RequestParam LocalDate fromDate,@RequestParam LocalDate tillDate) {
		return transactionService.getTxnBtwDateByAccId(accountId, fromDate, tillDate);
	}
	
	/*
	 * AIM: get transaction by accountId from given date
	 * PARAM: PathVariable -> accountId | RequestParam -> fromDate, page, size
	 * METHOD: GET
	 * RESPONSE: List<TransactionListDto>
	 * PATH: /api/transaction/get-from/{accountId}?fromDate=2025-06-03
	 * */
	@GetMapping("/get-from/{accountId}")
	public List<TransactionListDto> getTxnFromDateByAccId(@PathVariable int accountId,@RequestParam LocalDate fromDate) {
		return transactionService.getTxnFromDateByAccId(accountId, fromDate);
	}
	
	/*
	 * AIM: get last 10 transaction
	 * PARAM: PathVariable -> accountId
	 * METHOD: GET
	 * RESPONSE: List<TransactionListDto>
	 * PATH: /api/transaction/get-10/{accountId}
	 * */
	@GetMapping("/get-10/{accountId}")
	public List<TransactionListDto> getLast10TxnByAccId(@PathVariable int accountId) {
		return transactionService.getLast10TxnByAccId(accountId);
	}
	
	/*
	 * AIM: get ACCOUNT STATEMENT
	 * PARAM: PathVariable -> accountId | RequestParam -> fromDate, tillDate
	 * METHOD: GET
	 * RESPONSE: List<AccountStatement>
	 * PATH: /api/transaction/get/statement/{accountId}?fromDate=2025-06-07&tillDate=2025-06-09
	 * */
	@GetMapping("/get/statement/{accountId}")
	public AccountStatementDto getAccStmtBtwDateByAccId(@PathVariable int accountId, @RequestParam LocalDate fromDate, @RequestParam LocalDate tillDate) {
		return transactionService.getAccStmtBtwDatebyAccId(accountId, fromDate, tillDate);
	}
	
	/*
	 * AIM: get transaction btw date by branch id
	 * PARAM: PathVariable -> branchId | RequestParam -> fromDate, tillDate 
	 * METHOD: GET
	 * RESPONSE: List<Transaction>
	 * PATH: /api/transaction/get-btw/branchId/{branchId}?fromDate=2025-06-07&tillDate=2025-06-09
	 * */
	@GetMapping("/get-btw/branchId/{branchId}")
	public List<Transaction> getTxnBtwDateByBranchId(@PathVariable int branchId, @RequestParam LocalDate fromDate, @RequestParam LocalDate tillDate) {
		return transactionService.getTxnBtwDateByBranchId(branchId, fromDate, tillDate);
	}
	
	/*
	 * AIM: get transaction from date by branch id
	 * PARAM: PathVariable -> branchId | RequestParam -> fromDate 
	 * METHOD: GET
	 * RESPONSE: List<Transaction>
	 * PATH: /api/transaction/get-from/branchId/{branchId}?fromDate=2025-06-07
	 * */
	@GetMapping("/get-from/branchId/{branchId}")
	public List<Transaction> getTxnFromDateByBranchId(@PathVariable int branchId, @RequestParam LocalDate fromDate) {
		return transactionService.getTxnFromDateByBranchId(branchId, fromDate);
	}

	/*
	 * AIM: get transaction btw date
	 * PARAM: RequestParam -> fromDate, tillDate 
	 * METHOD: GET
	 * RESPONSE: List<Transaction>
	 * PATH: /api/transaction/get-btw?fromDate=2025-06-07&tillDate=2025-06-09
	 * */
	@GetMapping("/get-btw")
	public List<Transaction> getTransactionBetweenDate(@RequestParam LocalDate fromDate, @RequestParam LocalDate tillDate) {
		return transactionService.getTransactionBetweenDate(fromDate, tillDate);
	}
	
//	/*
//	 * AIM: get transaction from date
//	 * PARAM: RequestParam -> fromDate 
//	 * METHOD: GET
//	 * RESPONSE: List<Transaction>
//	 * PATH: /api/transaction/get-from?fromDate=2025-06-07
//	 * */
	@GetMapping("/get-from")
	public List<Transaction> getTransactionFromDate(@RequestParam LocalDate fromDate) {
		return transactionService.getTransactionsFromDate(fromDate);
	}
	
	/*
	 * AIM: get all transaction
	 * METHOD: GET
	 * RESPONSE: List<Transaction>
	 * PATH: /api/transaction/get-all
	 * */
	@GetMapping("/get-all")
	public List<Transaction> getAll() {
		return transactionService.getAll();
	}
}
