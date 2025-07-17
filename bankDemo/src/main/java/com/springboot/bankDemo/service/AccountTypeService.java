package com.springboot.bankDemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.repository.AccountTypeRepository;

@Service
public class AccountTypeService {

	private AccountTypeRepository accountTypeRepository;

	public AccountTypeService(AccountTypeRepository accountTypeRepository) {
		this.accountTypeRepository = accountTypeRepository;
	}
	
	// insert values into accountType
	public AccountType postAccountType(AccountType accountType) {
		return accountTypeRepository.save(accountType);
	}
	
	// update values in accountType
	public AccountType putAccountType(int id, AccountType accountType) {
		AccountType dbAccountType = accountTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		if(accountType.getType() != null)
			dbAccountType.setType(accountType.getType());
		if(accountType.getInitialDeposit() != null)
			dbAccountType.setInitialDeposit(accountType.getInitialDeposit());
		return accountTypeRepository.save(dbAccountType);
	}

	// fetch all accountType
	public List<AccountType> getAll() {
		return accountTypeRepository.findAll();
	}
	
	// fetch by id
	public AccountType getById(int id) {
		return accountTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
	}
	
	// fetch by account type name
	public AccountType getByType(String type) {
		return accountTypeRepository.getByType(type).orElseThrow();	// user defined method by JPQL 
//		return accountTypeRepository.findByType(type);	// Jpa built JPQL
	}
}
