package com.springboot.bankDemo.dto;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
@Component
public class TransactionDto {

	@NotBlank(message = "Amount cannot be blank")
	private BigDecimal amount;
	private String description;
		
	public TransactionDto() {	}

	public TransactionDto(BigDecimal amount, String description) {
		this.amount = amount;
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
