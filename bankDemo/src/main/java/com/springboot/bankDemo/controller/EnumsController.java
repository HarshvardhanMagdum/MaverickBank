package com.springboot.bankDemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.dto.TransferTypeDto;
import com.springboot.bankDemo.service.EnumsService;

@RestController
@RequestMapping("/api/enum")
@CrossOrigin(origins = "http://localhost:5173")
public class EnumsController {

	@Autowired
	private EnumsService enumsService;
	
	/*
	 * AIM: fetch all loan application status from enum
	* METHOD: GET
	 * RESPONSE: List<String>
	 * PATH: /api/enum/account/status/get
	 * */
	@GetMapping("/account/status/get")
	public List<String> getEnumAccountStatus() {
		return enumsService.getEnumAccountStatus();
	}
	
	/*
	 * AIM: fetch all loan application status from enum
	* METHOD: GET
	 * RESPONSE: List<String>
	 * PATH: /api/enum/loanApply/status/get
	 * */
	@GetMapping("/loanApply/status/get")
	public List<String> getEnumLoanApplicationStatus() {
		return enumsService.getEnumLoanApplicationStatus();
	}
	
	/*
	 * AIM: fetch all loan status from enum
	 * METHOD: GET
	 * RESPONSE: List<String>
	 * PATH: /api/enum/loan/status/get
	 * */
	@GetMapping("/loan/status/get")
	public List<String> getEnumLoanStatus() {
		return enumsService.getEnumLoanStatus();
	}
	
	/*
	 * AIM: fetch all loan type from enum
	 * METHOD: GET
	 * RESPONSE: List<String>
	 * PATH: /api/enum/laon/type/get
	 * */
	@GetMapping("/loan/type/get")
	public List<String> getEnumLoanType() {
		return enumsService.getEnumLoanType();
	}
	/*
	 * AIM: fetch all transfer type from enum
	 * METHOD: GET
	 * RESPONSE: List<String>
	 * PATH: /api/enum/transfer/type/get
	 * */
	@GetMapping("/transfer/type/get")
	public List<TransferTypeDto> getEnumTransferType() {
		return enumsService.getEnumTransferType();
	}
}
