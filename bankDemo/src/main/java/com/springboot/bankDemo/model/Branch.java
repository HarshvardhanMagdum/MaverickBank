package com.springboot.bankDemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "branch")
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name =  "ifsc_code", unique = true, nullable = false)
	private String ifscCode;
	@Column(name = "branch_name", nullable = false)
	private String branchName;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private String email;
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getIfscCode() { return ifscCode; }
	public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
	public String getBranchName() { return branchName; }
	public void setBranchName(String branchName) { this.branchName = branchName; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	
	
}
