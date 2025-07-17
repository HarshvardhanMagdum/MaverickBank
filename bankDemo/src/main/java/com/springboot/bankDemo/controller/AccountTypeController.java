package com.springboot.bankDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.model.AccountType;
import com.springboot.bankDemo.service.AccountTypeService;

@RestController
@RequestMapping("/api/accountType")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountTypeController {

	@Autowired
	private AccountTypeService accountTypeService;

	/*
	 * AIM: to insert values to accountType 
	 * METHOD: POST 
	 * PARAM: accountType -> RequestBody
	 * RESPONSE: AccountType 
	 * PATH: /api/accountType/post
	 */
	@PostMapping("/post")
	public ResponseEntity<?> postAccountType(@RequestBody AccountType accountType) {
		return ResponseEntity.status(HttpStatus.OK).body(accountTypeService.postAccountType(accountType));
	}
	
	/*
	 * AIM: update account type
	 * METHOD: PUT 
	 * PARAM: accountType -> RequestBody, id -> PathVariable
	 * RESPONSE: AccountType 
	 * PATH: /api/accountType/put/{id}
	 */
	@PutMapping("/put/{id}")
	public ResponseEntity<?> putAccountType(@PathVariable int id, @RequestBody AccountType accountType) {
		return ResponseEntity.status(HttpStatus.OK).body(accountTypeService.putAccountType(id, accountType));
	}
	
	/*
	 * AIM: fetch all accountType 
	 * METHOD: GET
	 * RESPONSE: List<AccountType> 
	 * PATH: /api/accountType/get-all
	 */
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(accountTypeService.getAll());
	}
	
	/*
	 * AIM: fetch by type of accountType 
	 * METHOD: GET
	 * PARAM: type -> RequestParam
	 * RESPONSE: AccountType 
	 * PATH: /api/accountType/get/type?type= SAVINGS or ...
	 */
	@GetMapping("/get/type")
	public ResponseEntity<?> getByType(@RequestParam String type) {
		return ResponseEntity.status(HttpStatus.OK).body(accountTypeService.getByType(type));
	}
	
	/*
	 * AIM: fetch by id
	 * METHOD: GET 
	 * PARAM: id -> PathVariable
	 * RESPONSE: AccountType 
	 * PATH: /api/accountType/get-one/{id}
	 */
	@GetMapping("/get-one/{id}")
	public AccountType getById(@PathVariable int id) {
		return accountTypeService.getById(id);
	}
	
}
