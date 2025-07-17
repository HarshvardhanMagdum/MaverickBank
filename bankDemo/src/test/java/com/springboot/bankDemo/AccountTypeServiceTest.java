package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.repository.AccountTypeRepository;
import com.springboot.bankDemo.service.AccountTypeService;

@SpringBootTest
public class AccountTypeServiceTest {

	@InjectMocks
	private AccountTypeService accountTypeService;

	@Mock
	private AccountTypeRepository accountTypeRepository;

	private AccountType accountType;

	@BeforeEach
	public void init() {
		accountType = new AccountType();
		accountType.setId(1);
		accountType.setType("SAVINGS");
		accountType.setInitialDeposit(BigDecimal.valueOf(1000));
	}

	@Test
	public void postAccountTypeTest() {
		when(accountTypeRepository.save(any(AccountType.class))).thenReturn(accountType);
		assertEquals(accountType, accountTypeService.postAccountType(accountType));
	}

	@Test
	public void putAccountTypeTest() {
		AccountType updated = new AccountType();
		updated.setType("CURRENT");
		updated.setInitialDeposit(BigDecimal.valueOf(5000));
		when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
		when(accountTypeRepository.save(any(AccountType.class))).thenReturn(updated);
		assertEquals(updated, accountTypeService.putAccountType(1, updated));
	}

	@Test
	public void getAllTest() {
		when(accountTypeRepository.findAll()).thenReturn(List.of(accountType));
		assertEquals(List.of(accountType), accountTypeService.getAll());
	}

	@Test
	public void getByIdTest() {
		when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
		assertEquals(accountType, accountTypeService.getById(1));
	}

	@Test
	public void getByTypeTest() {
		when(accountTypeRepository.getByType("SAVINGS")).thenReturn(Optional.of(accountType));
		assertEquals(accountType, accountTypeService.getByType("SAVINGS"));
	}

	@AfterEach
	public void afterTest() {
		accountType = null;
	}
}
