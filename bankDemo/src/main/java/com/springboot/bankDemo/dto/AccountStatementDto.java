package com.springboot.bankDemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AccountStatementDto {

	private int accountId;
	private String name;
	private LocalDate fromDate;
	private LocalDate toDate;
	private BigDecimal openingBalance;
	private BigDecimal closingBalance;
	private List<StatementListDto> statementListDtos;
	
	public AccountStatementDto() {	}

	public AccountStatementDto(int accountId, String name, LocalDate fromDate, LocalDate toDate,
			BigDecimal openingBalance, BigDecimal closingBalance, List<StatementListDto> statementListDtos) {
		this.accountId = accountId;
		this.name = name;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.statementListDtos = statementListDtos;
	}

	public int getAccountId() { return accountId; }
	public void setAccountId(int accountId) { this.accountId = accountId; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public LocalDate getFromDate() { return fromDate; }
	public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
	public LocalDate getToDate() { return toDate; }
	public void setToDate(LocalDate toDate) { this.toDate = toDate; }
	public BigDecimal getOpeningBalance() { return openingBalance; }
	public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
	public BigDecimal getClosingBalance() { return closingBalance; }
	public void setClosingBalance(BigDecimal closingBalance) { 	this.closingBalance = closingBalance; }
	public List<StatementListDto> getStatementListDtos() { return statementListDtos; }
	public void setStatementListDtos(List<StatementListDto> statementListDtos) { this.statementListDtos = statementListDtos; }
	
}
