package com.springboot.bankDemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.bankDemo.dto.ManagerCreateDto;
import com.springboot.bankDemo.model.Manager;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.ManagerRepository;

@Service
public class ManagerService {

	private ManagerRepository managerRepository;
	private UserService userService;

	public ManagerService(ManagerRepository managerRepository,UserService userService) {
		this.managerRepository = managerRepository;
		this.userService = userService;
	}

	// add manager
	public Manager postManager(ManagerCreateDto managerCreateDto) {
		User user = new User();
		user.setUsername(managerCreateDto.getUsername());
		user.setPassword(managerCreateDto.getPassword());
		user.setRole("MANAGER");
		user = userService.signUp(user);
		Manager manager = new Manager();
		manager.setFirstName(managerCreateDto.getFirstName());
		manager.setLastName(managerCreateDto.getLastName());
		manager.setEmail(managerCreateDto.getEmail());
		manager.setDateOfBirth(managerCreateDto.getDateOfBirth());
		manager.setGender(managerCreateDto.getGender());
		manager.setAddress(managerCreateDto.getAddress());
		manager.setPhoneNumber(managerCreateDto.getPhoneNumber());
		manager.setUser(user);
		return managerRepository.save(manager);
	}

	// update manager data
	public Manager putManager(String username, Manager manager) {
		Manager dbManager = managerRepository.getManagerByUsername(username);
		if (manager.getFirstName() != null)
			dbManager.setFirstName(manager.getFirstName());
		if (manager.getLastName() != null)
			dbManager.setLastName(manager.getLastName());
		if (manager.getEmail() != null)
			dbManager.setEmail(manager.getEmail());
		if (manager.getDateOfBirth() != null)
			dbManager.setDateOfBirth(manager.getDateOfBirth());
		if (manager.getGender() != null)
			dbManager.setGender(manager.getGender());
		if (manager.getPhoneNumber() != null)
			dbManager.setPhoneNumber(manager.getPhoneNumber());
		if (manager.getAddress() != null)
			dbManager.setAddress(manager.getAddress());
		return managerRepository.save(dbManager);
	}

	// get manager by username
	public Manager getByUsername(String username) {
		return managerRepository.getManagerByUsername(username);
	}

	// get all manager
	public List<Manager> getAll() {
		return managerRepository.findAll();
	}
}
