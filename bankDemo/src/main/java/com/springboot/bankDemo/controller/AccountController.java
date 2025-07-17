package com.springboot.bankDemo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.model.Account;
import com.springboot.bankDemo.service.AccountService;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

	@Autowired
	private AccountService accountService;

	/*
	 * AIM: create account by customer - login cred
	 * METHOD: POST
	 * PARAM: account -> RequestBody, PathVariable <- branch-ifscCode, accountType-type , Principal -> Username
	 * RESPONSE: Account 
	 * PATH: /api/account/post/?ifsc=MAVK001&type=SAVINGS
	 */
	@PostMapping("/post/")
	public Account postAccountByUsername(Principal principal, @RequestParam String ifscCode, 
							@RequestParam String type, @RequestBody Account account) {
		String username = principal.getName();
		return accountService.postAccountByUsername(username, ifscCode, type, account);
	}
	
	/*
	 * AIM: to insert values to account, which requires branchId,account-Type,customerId 
	 * METHOD: POST
	 * PARAM: account -> RequestBody || PathVariable <- customerId, branchId || RequestParam -> accountType-type 
	 * RESPONSE: Account
	 * PATH: /api/account/post/{customerId}/{branchId}?type=SAVING 
	 */
	@PostMapping("/post/{customerId}/{branchId}")
	public Account postAccountByCustomerId(@PathVariable int customerId, @PathVariable int branchId, 
							@RequestParam String type, @RequestBody Account account) {
		return accountService.postAccountByCustomerId(customerId, branchId, type, account);
	}
	
	/*
	 * AIM: update account status
	 * METHOD: PUT
	 * PARAM: PathVariable <- accountId, status  -> RequestParam
	 * RESPONSE: Account
	 * PATH: /api/account/put/status/{accountId}/?status=ACTIVE
	 */
	@PutMapping("/put/status/{accountId}/")
	public Account putAccountStatus(@PathVariable int accountId, @RequestParam String status) {
		return accountService.putAccountStatus(accountId, status);
	}
	
	/*
	 * AIM: add amount to account balance
	 * METHOD: PUT
	 * PARAM: PathVariable <- accountId, amtToAdd -> RequestParam
	 * RESPONSE: Account
	 * PATH: /api/account/put/balance/{accountId}/?amtToAdd=100
	 */
	@PutMapping("/put/balance/{accountId}")
	public Account putAccountBalance(@PathVariable int accountId, @RequestParam BigDecimal amtToAdd) {
		return accountService.putAccountBalance(accountId, amtToAdd);
	}
	
	/*
	 * AIM: fetch by accountId
	 * METHOD: GET
	 * PARAM:  accountId -> PathVariable
	 * RESPONSE: Account
	 * PATH: /api/account/get/id/{accountId}
	 */
	@GetMapping("/get/id/{accountId}")
	public Account getById(@PathVariable int accountId) {
		return accountService.getById(accountId);
	}
	
	/*
	 * AIM: fetch account by customerId
	 * METHOD: GET
	 * PARAM:  customerId -> PathVariable
	 * RESPONSE: List<Account>
	 * PATH: /api/account/get/customerId/{customerId}
	 */
	@GetMapping("/get/customerId/{customerId}")
	public List<Account> getByCustomerId(@PathVariable int customerId) {
		return accountService.getByCustomerId(customerId);
	}
	
	/*
	 * AIM: fetch account by IfscCode
	 * METHOD: GET
	 * PARAM:  PathVariable ->  BranchId
	 * RESPONSE: List<Account>
	 * PATH: /api/account/get/{branchId}
	 */
	@GetMapping("/get/{branchId}")
	public List<Account> getByIfscCode(@PathVariable int branchId) {
		return accountService.getByBranchId(branchId);
	}
	
	/*
	 * AIM: fetch account by username loggedIn credentials
	 * METHOD: GET
	 * PARAM:  Principal
	 * RESPONSE: List<Account>
	 * PATH: /api/account/get-one
	 */
	@GetMapping("/get-one")
	public List<Account> getByUsername(Principal principal) {
		String username = principal.getName();
		return accountService.getByUsername(username);
	}
	
	/*
	 * AIM: fetch account by status
	 * METHOD: GET
	 * PARAM:  @RequestParam
	 * RESPONSE: List<Account>
	 * PATH: /api/account/get/status?status:ACTIVE
	 */
	@GetMapping("/get/status")
	public List<Account> getByStatus(@RequestParam String status) {
		return accountService.getByStatus(status);
	}
	
	/*
	 * AIM: fetch all Account
	 * METHOD: GET 
	 * RESPONSE: Account 
	 * PATH: /api/account/get-all
	 */
	@GetMapping("/get-all")
	public List<Account> getAll() {
		return accountService.getAll();
	}
}
