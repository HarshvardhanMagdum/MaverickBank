package com.springboot.bankDemo.model;

import java.time.LocalDate;

import com.springboot.bankDemo.enums.LoanApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_application")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(optional = false)
	private LoanDetails loanDetails;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private LoanApplicationStatus status;
	@Column(name = "application_date", nullable = false)
	private LocalDate applicationDate;
	@ManyToOne(optional = false)
	private Account account;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoanDetails getLoanDetails() {
		return loanDetails;
	}
	public void setLoanDetails(LoanDetails loanDetails) {
		this.loanDetails = loanDetails;
	}
	public LoanApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(LoanApplicationStatus status) {
		this.status = status;
	}
	public LocalDate getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
		
}
