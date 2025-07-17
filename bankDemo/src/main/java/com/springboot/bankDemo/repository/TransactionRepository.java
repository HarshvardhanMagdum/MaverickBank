package com.springboot.bankDemo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankDemo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

	@Query("select t from Transaction t where t.account.id=?1 and t.transactionDate between ?2 and ?3 order by t.transactionDate asc")
	List<Transaction> getTxnBtwDateByAccId(int accountId, LocalDate fromDate, LocalDate tillDate, Pageable pageable);		// with pageable
	@Query("select t from Transaction t where t.account.id=?1 and t.transactionDate between ?2 and ?3")
	List<Transaction> getTxnBtwDateByAccId(int accountId, LocalDate fromDate, LocalDate tillDate);							// without pageable
	
	@Query("select t from Transaction t where t.account.id=?1 and t.transactionDate>=?2 order by t.transactionDate asc")
	List<Transaction> getTxnFromDateByAccId(int accountId, LocalDate fromDate, Pageable pageable);							// with pageable
	@Query("select t from Transaction t where t.account.id=?1 and t.transactionDate>=?2 order by t.transactionDate asc")
	List<Transaction> getTxnFromDateByAccId(int accountId, LocalDate fromDate);												// without pageable
	
	@Query("select t from Transaction t where t.account.id=?1 order by t.id desc")
	List<Transaction> getLast10TxnByAccId(int accountId, PageRequest limit);
	
	@Query("select t from Transaction t where t.account.branch.id=?1 and t.transactionDate  between ?2 and ?3 order by t.transactionDate asc")
	List<Transaction> getTxnBtwDateByBranchId(int branchId, LocalDate fromDate, LocalDate tillDate);
	
	@Query("select t from Transaction t where t.account.branch.id=?1 and t.transactionDate>=?2 order by t.transactionDate asc")
	List<Transaction> getTxnFromDateByBranchId(int branchId, LocalDate fromDate);
	
	
	@Query("select t from Transaction t where t.transactionDate between ?1 and ?2 order by t.transactionDate asc")
	List<Transaction> getTransactionBetweenDate(LocalDate fromDate, LocalDate tillDate);
	
	@Query("select t from Transaction t where t.transactionDate>=?1 order by t.transactionDate desc")
	List<Transaction> getTransactionFromDate(LocalDate fromDate);

}
