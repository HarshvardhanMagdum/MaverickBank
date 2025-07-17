package com.springboot.bankDemo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.springboot.bankDemo.enums.AccountStatus;

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
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(optional = false)
	private AccountType accountType;
	@Column(nullable = false)
	private BigDecimal balance;
	@Column(name = "open_date", nullable = false)
	private LocalDate openDate;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	@Column(name = "pan_number", nullable = false)
	private String panNumber;
	@Column(name = "aadhar_number", nullable = false)
	private String aadharNumber;
	@ManyToOne(optional = false)
	private Branch branch;
	@ManyToOne(optional = false)
	private Customer customer;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public AccountType getAccountType() { return accountType; }
	public void setAccountType(AccountType accountType) { this.accountType = accountType; }
	public BigDecimal getBalance() { return balance; }
	public void setBalance(BigDecimal balance) { this.balance = balance; }
	public LocalDate getOpenDate() { return openDate; }
	public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }
	public AccountStatus getStatus() { return status; }
	public void setStatus(AccountStatus status) { this.status = status; }
	public String getPanNumber() { return panNumber; }
	public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
	public String getAadharNumber() { return aadharNumber; }
	public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
	public Branch getBranch() { return branch; }
	public void setBranch(Branch branch) { this.branch = branch; }
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	
}
