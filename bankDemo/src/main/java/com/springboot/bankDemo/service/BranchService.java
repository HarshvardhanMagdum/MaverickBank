package com.springboot.bankDemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.repository.BranchRepository;

@Service
public class BranchService {

	private BranchRepository branchRepository;

	public BranchService(BranchRepository branchRepository) {
		this.branchRepository = branchRepository;
	}

	// insert values into branch
	public Branch postBranch(Branch branch) {
		return branchRepository.save(branch);
	}

	// fetch all branch
	public List<Branch> getAll() {
		return branchRepository.findAll();
	}

	// fetch branch by ifsc_code
	public Branch getByIfscCode(String ifscCode) {
		return branchRepository								// using method created by user wih JPQL
				.getByIfscCode(ifscCode)
				.orElseThrow(() -> new RuntimeException("IFSC Code is Invalid"));
//		return branchRepository.findByIfscCode(ifscCode);	// using method which itself write the JPQL
	}

	// fetch branch by id
	public Branch getById(int id) {
		return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Id is Invalid"));
	}

	// update branch
	public Branch putBranch(int id, Branch branch) {
		Branch dbBranch = branchRepository.findById(id).orElseThrow(() ->  new RuntimeException("ID is Invalid"));
		if(branch.getAddress()!=null)
			dbBranch.setAddress(branch.getAddress());
		if(branch.getEmail()!=null)
			dbBranch.setEmail(branch.getEmail());
		if(branch.getPhoneNumber()!=null)
			dbBranch.setPhoneNumber(branch.getPhoneNumber());
		return branchRepository.save(dbBranch);
	}
}
