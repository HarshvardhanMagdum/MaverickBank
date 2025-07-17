package com.springboot.bankDemo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_repayment")
public class LoanRepayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "repayment_amount", nullable = false)
	private BigDecimal repaymentAmount;
	@Column(name = "repayment_date", nullable = false)
	private LocalDate repaymentDate;
	@ManyToOne(optional = false)
	private Loan loan;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public BigDecimal getRepaymentAmount() { return repaymentAmount; }
	public void setRepaymentAmount(BigDecimal repaymentAmount) { this.repaymentAmount = repaymentAmount; }
	public LocalDate getRepaymentDate() { return repaymentDate; }
	public void setRepaymentDate(LocalDate repaymentDate) { this.repaymentDate = repaymentDate; }
	public Loan getLoan() { return loan; }
	public void setLoan(Loan loan) { this.loan = loan; }
	
}
