package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.repository.BranchRepository;
import com.springboot.bankDemo.service.BranchService;

@SpringBootTest
public class BranchServiceTest {

	@InjectMocks
	private BranchService branchService;
	@Mock
	private BranchRepository branchRepository;
	
	private Branch branch;

	@BeforeEach
	public void init() {
		branch = new Branch();
		branch.setId(1);
		branch.setIfscCode("IFSC0003");
		branch.setBranchName("Coimbatore");
		branch.setAddress("Gandhipuram");
		branch.setEmail("cbe@bank.com");
		branch.setPhoneNumber("9876543211");
	}

	@Test
	public void postBranchTest() {
		when(branchRepository.save(any(Branch.class))).thenReturn(branch);
		assertEquals(branch, branchService.postBranch(branch));
	}

	@Test
	public void getAllBranchesTest() {
		when(branchRepository.findAll()).thenReturn(List.of(branch));
		assertEquals(List.of(branch), branchService.getAll());
	}

	@Test
	public void getByIfscCodeTest() {
		when(branchRepository.getByIfscCode("IFSC0003")).thenReturn(Optional.of(branch));
		// actual
		assertEquals(branch, branchService.getByIfscCode("IFSC0003"));
		
		// use case ifsc code is invalid
		RuntimeException e = assertThrows(RuntimeException.class,
				() -> branchService.getByIfscCode("IFSC01"));
		assertEquals("IFSC Code is Invalid", e.getMessage());
	}

	@Test
	public void getByIdTest() {
		when(branchRepository.findById(1)).thenReturn(Optional.of(branch));
		// actual
		assertEquals(branch, branchService.getById(1));
		
		// use case id is invalid
		RuntimeException e = assertThrows(RuntimeException.class,
				() -> branchService.getById(5));
		assertEquals("Id is Invalid", e.getMessage());
	}

	@Test
	public void putBranchTest() {
		Branch updated = new Branch();
		updated.setAddress("Race Course");
		updated.setEmail("update@bank.com");
		updated.setPhoneNumber("9999999990");

		when(branchRepository.findById(1)).thenReturn(Optional.of(branch));
		when(branchRepository.save(any(Branch.class))).thenAnswer(inv -> inv.getArgument(0));

		Branch result = branchService.putBranch(1, updated);
		assertEquals("Race Course", result.getAddress());
		assertEquals("update@bank.com", result.getEmail());
		assertEquals("9999999990", result.getPhoneNumber());
	}

	@AfterEach
	public void afterTest() {
		branch = null;
	}
}
