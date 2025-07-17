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

import com.springboot.bankDemo.dto.ManagerCreateDto;
import com.springboot.bankDemo.model.Manager;
import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.repository.ManagerRepository;
import com.springboot.bankDemo.service.ManagerService;
import com.springboot.bankDemo.service.UserService;

@SpringBootTest
public class ManagerServiceTest {

	@InjectMocks
	private ManagerService managerService;
	@Mock
	private ManagerRepository managerRepository;
	@Mock
	private UserService userService;

	private User user;
	private Manager manager;
	private ManagerCreateDto managerCreateDto;

	@BeforeEach
	public void init() {
		managerCreateDto = new ManagerCreateDto();
        managerCreateDto.setFirstName("John");
        managerCreateDto.setLastName("Doe");
        managerCreateDto.setEmail("john.doe@example.com");
        managerCreateDto.setPhoneNumber("1234567890");
        managerCreateDto.setDateOfBirth(LocalDate.now());
        managerCreateDto.setGender("MALE");
        managerCreateDto.setAddress("Chennai");
        managerCreateDto.setUsername("johnmanager");
        managerCreateDto.setPassword("securepass");

        user = new User();
        user.setId(1);
        user.setUsername("johnmanager");
        user.setPassword("encodedpass");
        user.setRole("MANAGER");

        manager = new Manager();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setEmail("john.doe@example.com");
        manager.setDateOfBirth(LocalDate.now());
        manager.setGender("MALE");
        manager.setPhoneNumber("1234567890");
        manager.setAddress("Chennai");
        manager.setUser(user);
	}

	@Test
	public void postManagerTest() {
		when(userService.signUp(any(User.class))).thenReturn(user);
		when(managerRepository.save(any(Manager.class))).thenReturn(manager);
		assertEquals(manager, managerService.postManager(managerCreateDto));
	}

	@Test
	public void getByUsernameTest() {
		when(managerRepository.getManagerByUsername("johnmanager")).thenReturn(manager);
		assertEquals(manager, managerService.getByUsername("johnmanager"));
	}

	@Test
	public void getAllTest() {
		List<Manager> list = List.of(manager);
		when(managerRepository.findAll()).thenReturn(list);
		assertEquals(list, managerService.getAll());
	}
	
	@Test
	public void putManagerTest() {
		Manager existingManager = new Manager();
        existingManager.setFirstName("Old");
        existingManager.setLastName("Name");
        existingManager.setEmail("old@example.com");
        existingManager.setPhoneNumber("1111111111");
        existingManager.setAddress("Old Address");
        existingManager.setUser(user);

        when(managerRepository.getManagerByUsername("johnmanager")).thenReturn(existingManager);
        when(managerRepository.save(any(Manager.class))).thenReturn(existingManager);

        manager.setFirstName("Updated");
        manager.setLastName(null); // should remain unchanged
        manager.setEmail("updated@example.com");
        manager.setPhoneNumber("9999999999");
        manager.setAddress("New Address");

        Manager result = managerService.putManager("johnmanager", manager);

        assertEquals("Updated", result.getFirstName());
        assertEquals("Name", result.getLastName()); // unchanged
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("9999999999", result.getPhoneNumber());
        assertEquals("New Address", result.getAddress());
	}

	@AfterEach
	public void afterTest() {
		user = null;
		manager = null;
		managerCreateDto = null;
	}
}
