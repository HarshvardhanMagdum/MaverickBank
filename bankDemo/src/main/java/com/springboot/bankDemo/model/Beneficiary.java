package com.springboot.bankDemo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "beneficiary")
public class Beneficiary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String name;
	@Column(name = "account_number", nullable = false)
	private int accountNumber;
	@Column(name = "ifsc_code", nullable = false)
	private String ifscCode;
	@Column(name = "branch_name", nullable = false)
	private String branchName;
	private String description;
	@Column(name = "added_on", nullable = false)
	private LocalDate addedOn;
	@ManyToOne(optional = false)
	private Customer customer;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getAccountNumber() { return accountNumber; }
	public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }
	public String getIfscCode() { return ifscCode; }
	public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
	public String getBranchName() { return branchName; }
	public void setBranchName(String branchName) { this.branchName = branchName; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public LocalDate getAddedOn() { return addedOn; }
	public void setAddedOn(LocalDate addedOn) { this.addedOn = addedOn; }
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	
}
