package com.springboot.bankDemo.dto;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class TransferTypeDto {

	private String type;
	private BigDecimal charge;
	public TransferTypeDto() {	}
	public TransferTypeDto(String type, BigDecimal charge) {
		this.type = type;
		this.charge = charge;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
}
