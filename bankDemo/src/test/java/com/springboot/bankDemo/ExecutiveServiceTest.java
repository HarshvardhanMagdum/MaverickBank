package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.dto.ExecutiveCreateDto;
import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.model.Executive;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.ExecutiveRepository;
import com.springboot.bankDemo.service.BranchService;
import com.springboot.bankDemo.service.ExecutiveService;
import com.springboot.bankDemo.service.UserService;

@SpringBootTest
public class ExecutiveServiceTest {

	@InjectMocks
	private ExecutiveService executiveService;
	@Mock
	private ExecutiveRepository executiveRepository;
	@Mock
	private BranchService branchService;
	@Mock
	private UserService userService;

	private User user;
	private Branch branch;
	private Executive executive;
	private ExecutiveCreateDto executiveCreateDto;
	
	@BeforeEach
	public void init() {
		user = new User();
		user.setId(1);
		user.setUsername("john@gmail.com");
		user.setPassword("john@123");
		user.setRole("EXECUTIVE");

		branch = new Branch();
		branch.setId(1);
		branch.setIfscCode("IFSC0001");
		branch.setBranchName("Chennai Main");
		branch.setAddress("No. 12, Mount Road, Chennai");
		branch.setEmail("chennai@ifsc.com");
		branch.setPhoneNumber("04412345678");

		executive = new Executive();
		executive.setId(1);
		executive.setFirstName("John");
		executive.setLastName("Doe");
		executive.setEmail("john@gmail.com");
		executive.setPhoneNumber("9876543210");
		executive.setDateOfBirth(LocalDate.now());
		executive.setGender("MALE");
		executive.setAddress("Chennai");
		executive.setBranch(branch);
		executive.setUser(user);

		executiveCreateDto = new ExecutiveCreateDto();
		executiveCreateDto.setFirstName("John");
		executiveCreateDto.setLastName("Doe");
		executiveCreateDto.setEmail("john@gmail.com");
		executiveCreateDto.setPhoneNumber("9876543210");
		executiveCreateDto.setDateOfBirth(LocalDate.now());
		executiveCreateDto.setGender("MALE");
		executiveCreateDto.setAddress("Chennai");
		executiveCreateDto.setUsername("john@gmail.com");
		executiveCreateDto.setPassword("john@123");
	}

	@Test
	public void postExecutiveTest() {
		when(branchService.getById(1)).thenReturn(branch);
		when(userService.signUp(any(User.class))).thenReturn(user);
		when(executiveRepository.save(any(Executive.class))).thenReturn(executive);
		assertEquals(executive, executiveService.postExecutive(1, executiveCreateDto));
	}

	@Test
	public void getByUsernameTest() {
		when(executiveRepository.getExecutiveByUsername("john@gmail.com")).thenReturn(executive);
		assertEquals(executive, executiveService.getByUsername("john@gmail.com"));
	}

	@Test
	public void getByBranchTest() {
		List<Executive> list = List.of(executive);
		when(executiveRepository.getExecutiveByBranch(1)).thenReturn(list);
		assertEquals(list, executiveService.getByBranch(1));
	}

	@Test
	public void getAllTest() {	
		List<Executive> list = List.of(executive);
		when(executiveRepository.findAll()).thenReturn(list);
		assertEquals(list, executiveService.getAll());
	}

	@Test
	public void putExecutiveTest() {
		Executive updated = new Executive();
		updated.setFirstName("Johnny");
		updated.setLastName("Doel");
		updated.setEmail("johnny@gmail.com");
		updated.setPhoneNumber("9999999999");
		updated.setAddress("Bangalore");
		
		when(executiveRepository.getExecutiveByUsername("john@gmail.com")).thenReturn(executive);
		when(executiveRepository.save(any(Executive.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		Executive result = executiveService.putExecutive("john@gmail.com", updated);
		assertEquals("Johnny", result.getFirstName());
		assertEquals("Doel", result.getLastName());
		assertEquals("johnny@gmail.com", result.getEmail());
		assertEquals("9999999999", result.getPhoneNumber());
		assertEquals("Bangalore", result.getAddress());
	}

	@AfterEach
	public void afterTest() {
		user = null;
		executive = null;
		executiveCreateDto = null;
		branch = null;
	}
	
}
