package com.springboot.bankDemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.springboot.bankDemo.enums.LoanType;
import com.springboot.bankDemo.exception.InvalidInputException;
import com.springboot.bankDemo.model.LoanDetails;
import com.springboot.bankDemo.repository.LoanDetailsRepository;
import com.springboot.bankDemo.service.LoanDetailsSevice;

@SpringBootTest
public class LoanDetailsServiceTest {

	@InjectMocks
	private LoanDetailsSevice loanDetailsService;
	@Mock
	private LoanDetailsRepository loanDetailsRepository;

	private LoanDetails loanDetails;

	@BeforeEach
	public void init() {
		loanDetails = new LoanDetails();
		loanDetails.setId(1);
		loanDetails.setLoanType(LoanType.HOME);
		loanDetails.setPrincipalAmount(new BigDecimal("100000"));
		loanDetails.setInterestRate(new BigDecimal("10")); // 10%
		loanDetails.setTermInMonth(12);
		loanDetails.setTotalRepayableAmount(BigDecimal.valueOf(110000.00));
		loanDetails.setEmiAmount(BigDecimal.valueOf(9166.67));
	}

	@Test
	public void postLoanDetailsTest() throws InvalidInputException {
		when(loanDetailsRepository.save(any(LoanDetails.class))).thenReturn(loanDetails);
		// actual
		assertEquals(loanDetails, loanDetailsService.postLoanDetails(loanDetails));
		
		// use case Principal amount cannot be negative or zero
		loanDetails.setPrincipalAmount(BigDecimal.ZERO);
		InvalidInputException e = assertThrows(InvalidInputException.class,
				() -> loanDetailsService.postLoanDetails(loanDetails));
		assertEquals("Principal amount cannot be negative or zero", e.getMessage());
		
		// use case Interest Rate cannot be negative or zero
		loanDetails.setPrincipalAmount(new BigDecimal("100000"));
		loanDetails.setInterestRate(BigDecimal.ZERO);
		e = assertThrows(InvalidInputException.class, () -> loanDetailsService.postLoanDetails(loanDetails));
		assertEquals("Interest Rate cannot be negative or zero", e.getMessage());
		
		// use case Term in month cannot be negative or zero
		loanDetails.setInterestRate(new BigDecimal("10"));
		loanDetails.setTermInMonth(0);
		e = assertThrows(InvalidInputException.class, () -> loanDetailsService.postLoanDetails(loanDetails));
		assertEquals("Term in month cannot be negative or zero", e.getMessage());
	}

	@Test
	public void getByIdTest() {
		when(loanDetailsRepository.findById(1)).thenReturn(Optional.of(loanDetails));
		// actual
		assertEquals(loanDetails, loanDetailsService.getById(1));
		
		// use case id is invalid
		when(loanDetailsRepository.findById(2)).thenReturn(Optional.empty());
		RuntimeException e = assertThrows(RuntimeException.class, () -> loanDetailsService.getById(2));
		assertEquals("ID is Invalid", e.getMessage());
	}

	@Test
	public void getAllTest() {
		when(loanDetailsRepository.findAll()).thenReturn(List.of(loanDetails));
		assertEquals(List.of(loanDetails), loanDetailsService.getAll());
	}

	@Test
	public void putLoanDetails() throws InvalidInputException {
		LoanDetails updated = new LoanDetails();
		updated.setPrincipalAmount(new BigDecimal("200000"));
		when(loanDetailsRepository.findById(1)).thenReturn(Optional.of(loanDetails));
		when(loanDetailsRepository.save(any(LoanDetails.class))).thenReturn(loanDetails);
		LoanDetails result = loanDetailsService.putLoanDetails(1, updated);
		// actual
		assertEquals(new BigDecimal("200000"), result.getPrincipalAmount());
		
		// use case Principal amount cannot be negative or zero
		updated.setPrincipalAmount(BigDecimal.ZERO);
		InvalidInputException e = assertThrows(InvalidInputException.class,
				() -> loanDetailsService.putLoanDetails(1, updated));
		assertEquals("Principal amount cannot be negative or zero", e.getMessage());
		
		// use case Interest Rate cannot be negative or zero
		updated.setPrincipalAmount(new BigDecimal("200000"));
		updated.setInterestRate(BigDecimal.ZERO);
		e = assertThrows(InvalidInputException.class, () -> loanDetailsService.putLoanDetails(1, updated));
		assertEquals("Interest Rate cannot be negative or zero", e.getMessage());
		
		// use case Term in month cannot be negative
		updated.setInterestRate(new BigDecimal("10"));
		updated.setTermInMonth(-10);
		e = assertThrows(InvalidInputException.class, () -> loanDetailsService.putLoanDetails(1, updated));
		assertEquals("Term in month cannot be negative", e.getMessage());
		
		// use case id is invalid
		updated.setTermInMonth(20);
		when(loanDetailsRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loanDetailsService.putLoanDetails(99, loanDetails));
        assertEquals("ID is Invalid", ex.getMessage());
	}

	@AfterEach
	public void afterTest() {
		loanDetails = null;
	}
}
