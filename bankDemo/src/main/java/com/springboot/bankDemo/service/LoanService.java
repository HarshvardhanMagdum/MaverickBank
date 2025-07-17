package com.springboot.bankDemo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.enums.LoanApplicationStatus;
import com.springboot.bankDemo.enums.LoanStatus;
import com.springboot.bankDemo.model.Loan;
import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.repository.LoanRepository;

@Service
public class LoanService {

	private LoanRepository loanRepository;
	private TransactionService transactionService;
	
	public LoanService(LoanRepository loanRepository, TransactionService transactionService) {
		this.loanRepository = loanRepository;
		this.transactionService = transactionService;
	}
	
	// post loan with loan application
	public Loan postLoan(LoanApplication loanApplication) {
		Loan loan = new Loan();
		if(!loanApplication.getStatus().equals(LoanApplicationStatus.APPROVED))
			throw new RuntimeException("LoanApplication is not Approved");
		loan.setStatus(LoanStatus.ACTIVE);
		loan.setBalanceAmount(loanApplication.getLoanDetails().getPrincipalAmount());
		loan.setStartDate(LocalDate.now());
		loan.setEndDate(LocalDate.now().plusMonths(loanApplication.getLoanDetails().getTermInMonth()));
		loan.setLoanApplication(loanApplication);
		transactionService.postLoanDeposit(loanApplication.getAccount().getId(), 
									loanApplication.getLoanDetails().getPrincipalAmount());
		return loanRepository.save(loan);
	}
	
	// update loan balance
	public Loan putLoanBalance(int id, BigDecimal paidAmount) {
		Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		loan.setBalanceAmount(loan.getBalanceAmount().subtract(paidAmount));
		return loanRepository.save(loan);
	}
	
	// update loan status
	public Loan putLoanStatus(int id, String status) {
		Loan loan = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		loan.setStatus(LoanStatus.valueOf(status));
		return loanRepository.save(loan);
	}
	
	// fetch loan by branchId
	public List<Loan> getByBranchId(int branchId) {
		return loanRepository.getByBranchId(branchId);
	}
	
	// fetch loan by ID
	public Loan getById(int id) {
		return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
	}
	
	// fetch all loan
	public List<Loan> getAll() {
		return loanRepository.findAll();
	}

	// fetch by customer 
	public List<Loan> getByCustomer(String username) {
		return loanRepository.getByCustomer(username);
	}
}
