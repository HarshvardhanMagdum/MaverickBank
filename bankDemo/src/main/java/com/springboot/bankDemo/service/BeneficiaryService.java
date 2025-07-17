package com.springboot.bankDemo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.enums.AccountStatus;
import com.springboot.bankDemo.exception.ResourceNotFoundException;
import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.model.Beneficiary;
import com.springboot.bankDemo.model.Customer;
import com.springboot.bankDemo.repository.AccountRepository;
import com.springboot.bankDemo.repository.BeneficiaryRepository;
import com.springboot.bankDemo.repository.BranchRepository;
import com.springboot.bankDemo.repository.CustomerRepository;

@Service
public class BeneficiaryService {

	private BeneficiaryRepository beneficiaryRepository;
	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private BranchRepository branchRepository;
	
	public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, CustomerRepository customerRepository,
			AccountRepository accountRepository, BranchRepository branchRepository) {
		this.beneficiaryRepository = beneficiaryRepository;
		this.customerRepository = customerRepository;
		this.accountRepository =  accountRepository;
		this.branchRepository = branchRepository;
	}

	// insert values into beneficiary
	public Beneficiary postBeneficiary(String username, Beneficiary beneficiary) {
		Customer customer = customerRepository.getCustomerByUsername(username);
		Account account = accountRepository.findById(beneficiary.getAccountNumber()).
				orElseThrow(() -> new RuntimeException("Invalid Account ID"));
		if (account.getStatus()!=AccountStatus.ACTIVE)
			throw new RuntimeException("Account is not active");
		branchRepository.getByIfscCode(beneficiary.getIfscCode()).orElseThrow(() -> new RuntimeException("Invalid IFSC Code"));
		branchRepository.getBranchByNameIfsc(beneficiary.getIfscCode(), beneficiary.getBranchName())
											.orElseThrow(() -> new RuntimeException("IFSC Code and Branch Name Mismatching"));
		beneficiary.setCustomer(customer);
		beneficiary.setAddedOn(LocalDate.now());
		return beneficiaryRepository.save(beneficiary);
	}

	// update beneficiary
	public Beneficiary putBeneficiary(int id, int customerId, Beneficiary beneficiary) {
		Beneficiary dbBeneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer ID is Invalid"));
		if(beneficiary.getName() != null)
			dbBeneficiary.setName(beneficiary.getName());
		if(beneficiary.getAccountNumber() != 0)
			dbBeneficiary.setAccountNumber(beneficiary.getAccountNumber());
		if(beneficiary.getIfscCode() != null)
			dbBeneficiary.setIfscCode(beneficiary.getIfscCode());
		if(beneficiary.getBranchName() != null)
			dbBeneficiary.setBranchName(beneficiary.getBranchName());
		if(beneficiary.getDescription() != null)
			dbBeneficiary.setDescription(beneficiary.getDescription());
		dbBeneficiary.setCustomer(customer);
		return beneficiaryRepository.save(dbBeneficiary);
	}
	
	// fetch by customer id
	public List<Beneficiary> getByCustomerUsername(String username) throws ResourceNotFoundException {
		Customer customer = customerRepository.getCustomerByUsername(username);
		return beneficiaryRepository.getByCustomerId(customer.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));  // user writes JPQL
//		return beneficiaryRepository.findByCustomerId(customerId)
//				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));  // Jpa writes JPQL
	}
	
	// fetch by id
	public Beneficiary getById(int id) {
		return beneficiaryRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
	}
	
	// fetch all beneficiary
	public List<Beneficiary> getAll() {
		return beneficiaryRepository.findAll();
	}

	// delete beneficiary by id
	public void deleteById(int id) {
		beneficiaryRepository.findById(id).orElseThrow(() -> new RuntimeException("ID is Invalid"));
		beneficiaryRepository.deleteById(id);;
	}
}
