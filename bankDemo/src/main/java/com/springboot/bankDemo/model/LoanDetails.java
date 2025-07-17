package com.springboot.bankDemo.model;

import java.math.BigDecimal;

import com.springboot.bankDemo.enums.LoanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_details")
public class LoanDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	@Column(name = "loan_type", nullable = false)
	private LoanType loanType;
	@Column(name = "principal_amount", nullable = false)
	private BigDecimal principalAmount;
	@Column(name = "interest_rate", nullable = false)
	private BigDecimal interestRate;
	@Column(name = "term_in_month", nullable = false)
	private int termInMonth;
	@Column(name = "total_repayable_amount", nullable = false)
	private BigDecimal totalRepayableAmount;
	@Column(name = "emi_amount", nullable = false)
	private BigDecimal emiAmount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoanType getLoanType() {
		return loanType;
	}
	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}
	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public int getTermInMonth() {
		return termInMonth;
	}
	public void setTermInMonth(int termInMonth) {
		this.termInMonth = termInMonth;
	}
	public BigDecimal getTotalRepayableAmount() {
		return totalRepayableAmount;
	}
	public void setTotalRepayableAmount(BigDecimal totalRepayableAmount) {
		this.totalRepayableAmount = totalRepayableAmount;
	}
	public BigDecimal getEmiAmount() {
		return emiAmount;
	}
	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}
	
	
}
