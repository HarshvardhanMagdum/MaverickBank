package com.springboot.bankDemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.dto.ExecutiveCreateDto;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Executive;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.ExecutiveRepository;

@Service
public class ExecutiveService {

	private ExecutiveRepository executiveRepository;
	private BranchService branchService;
	private UserService userService;

	public ExecutiveService(ExecutiveRepository executiveRepository,
			BranchService branchService, UserService userService) {
		this.executiveRepository = executiveRepository;
		this.branchService = branchService;
		this.userService = userService;
	}

	// add customer-executive
	public Executive postExecutive(int branchId, ExecutiveCreateDto executiveCreateDto) {
		Branch branch = branchService.getById(branchId);
		User user = new User();
		user.setUsername(executiveCreateDto.getUsername());
		user.setPassword(executiveCreateDto.getPassword());
		user.setRole("EXECUTIVE");
		user = userService.signUp(user);
		Executive executive = new Executive();
		executive.setFirstName(executiveCreateDto.getFirstName());
		executive.setLastName(executiveCreateDto.getLastName());
		executive.setEmail(executiveCreateDto.getEmail());
		executive.setDateOfBirth(executiveCreateDto.getDateOfBirth());
		executive.setGender(executiveCreateDto.getGender());
		executive.setAddress(executiveCreateDto.getAddress());
		executive.setPhoneNumber(executiveCreateDto.getPhoneNumber());
		executive.setBranch(branch);
		executive.setUser(user);
		return executiveRepository.save(executive);
	}

	// update customer-executive data
	public Executive putExecutive(String username, Executive executive) {
		Executive dbExecutive = executiveRepository.getExecutiveByUsername(username);
		if (executive.getFirstName() != null)
			dbExecutive.setFirstName(executive.getFirstName());
		if (executive.getLastName() != null)
			dbExecutive.setLastName(executive.getLastName());
		if (executive.getEmail() != null)
			dbExecutive.setEmail(executive.getEmail());
		if (executive.getDateOfBirth() != null)
			dbExecutive.setDateOfBirth(executive.getDateOfBirth());
		if (executive.getGender() != null)
			dbExecutive.setGender(executive.getGender());
		if (executive.getPhoneNumber() != null)
			dbExecutive.setPhoneNumber(executive.getPhoneNumber());
		if (executive.getAddress() != null)
			dbExecutive.setAddress(executive.getAddress());
		return executiveRepository.save(dbExecutive);
	}

	// get customer executive by username
	public Executive getByUsername(String username) {
		return executiveRepository.getExecutiveByUsername(username);
	}

	// get customer executive under a branch
	public List<Executive> getByBranch(int branchId) {
		return executiveRepository.getExecutiveByBranch(branchId);
	}
	// fetch all executives
	public List<Executive> getAll() {
		return executiveRepository.findAll();
	}
}
