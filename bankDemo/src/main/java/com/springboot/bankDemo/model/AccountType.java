package com.springboot.bankDemo.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_type")
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String type;
	@Column(name = "initial_deposit", nullable = false)
	private BigDecimal initialDeposit;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public BigDecimal getInitialDeposit() { return initialDeposit; }
	public void setInitialDeposit(BigDecimal initialDeposit) { this.initialDeposit = initialDeposit; }
	
}
