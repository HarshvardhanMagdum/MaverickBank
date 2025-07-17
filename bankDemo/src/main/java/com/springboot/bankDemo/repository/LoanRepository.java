package com.springboot.bankDemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankDemo.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer>{

	@Query("select l from Loan l where l.loanApplication.account.branch.id=?1")
	List<Loan> getByBranchId(int branchId);

	@Query("select l from Loan l where l.loanApplication.account.customer.user.username=?1")
	List<Loan> getByCustomer(String username);

}
