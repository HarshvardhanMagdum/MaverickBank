package com.springboot.bankDemo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.springboot.bankDemo.enums.EntryType;

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
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "transaction_type", nullable = false)
	private String transactionType;
	@Column(name = "transaction_date", nullable = false)
	private LocalDate transactionDate;
	@Column(name = "transfer_account_id", nullable = false)
	private int transferAccountId;
	@Column(nullable = false)
	private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	@Column(name = "entry_type", nullable = false)
	private EntryType entryType;
	private String description;
	@Column(name = "balance_after_transaction", nullable = false)
	private BigDecimal balanceAfterTxn;
	@ManyToOne(optional = false)
	private Account account;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getTransactionType() { return transactionType; }
	public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
	public LocalDate getTransactionDate() { return transactionDate; }
	public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
	public int getTransferAccountId() { return transferAccountId; }
	public void setTransferAccountId(int transferAccountId) { this.transferAccountId = transferAccountId; }
	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	public EntryType getEntryType() { return entryType; }
	public void setEntryType(EntryType entryType) { this.entryType = entryType; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public BigDecimal getBalanceAfterTxn() { return balanceAfterTxn; }
	public void setBalanceAfterTxn(BigDecimal balanceAfterTxn) { this.balanceAfterTxn = balanceAfterTxn; }
	public Account getAccount() { return account; }
	public void setAccount(Account account) { this.account = account; }
		
}
