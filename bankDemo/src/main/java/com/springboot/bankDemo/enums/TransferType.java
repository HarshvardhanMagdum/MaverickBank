package com.springboot.bankDemo.enums;

import java.math.BigDecimal;

public enum TransferType {

	NEFT(new BigDecimal(2.00)),
	IMPS(new BigDecimal(5.00)),
	RTGS(new BigDecimal(10.00));
	
	private final BigDecimal charge;

	private TransferType(BigDecimal charge) {
		this.charge = charge;
	}

	public BigDecimal getCharge() {
		return charge;
	}
	
}
