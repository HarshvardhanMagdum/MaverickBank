package com.springboot.bankDemo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.service.LoanApplicationService;

@RestController
@RequestMapping("/api/loanApply")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanApplicationController {

	@Autowired
	private LoanApplicationService loanApplicationService;
	
	/*
	 * AIM: post loan application
	 * METHOD: POST
	 * PARAM: PathVariable - > LoanDetailsId, AccountId
	 * RESPONSE: LoanApplication
	 * PATH: /api/loanApply/post/{loanDetailsId}/{accountId}
	 * */
	@PostMapping("/post/{loanDetailsId}/{accountId}")
	public LoanApplication postLoanApplication(@PathVariable int loanDetailsId, @PathVariable int accountId) {
		return loanApplicationService.postLoanApplication(loanDetailsId, accountId);
	}
	
	/*
	 * AIM: update status by loan id
	 * METHOD: PUT
	 * PARAM: PathVariable -> ID
	 * RESPONSE: LoanApplication
	 * PATH: /api/loanApply/put/status/cancelled/{id}
	 * */
	@PutMapping("/put/status/cancelled/{id}")
	public LoanApplication putLoanApplicationStatusCancelled(@PathVariable int id) {
		return loanApplicationService.putLoanApplicationStatusCancelled(id);
	}
	
	/*
	 * AIM: update loan status
	 * METHOD: PUT
	 * PARAM: PathVariable -> ID | RequestParam -> status
	 * RESPONSE: LoanApplication
	 * PATH: /api/loanApply/put/status/{id}?status=APPROVED
	 * */
	@PutMapping("/put/status/{id}")
	public LoanApplication putLoanApplicationStatus(@PathVariable int id, @RequestParam String status) {
		return loanApplicationService.putLoanApplicationStatus(id, status);
	}
	
	/*
	 * AIM: to fetch loan application with user login-cred
	 * METHOD: GET
	 * PARAM: Principal 
	 * RESPONSE: List<LoanApplication>
	 * PATH: /api/loanApply/get-one
	 * */
	@GetMapping("/get-one")
	public List<LoanApplication> getByUsername(Principal principal) {
		String username = principal.getName();
		return loanApplicationService.getByUsername(username);
	}
	
	/*
	 * AIM: to fetch loan application by status
	 * METHOD: GET
	 * PARAM: RequestParam -> status
	 * RESPONSE: List<LoanApplication>
	 *PATH: /api/loanApply/get-by/status?status=DECLINED
	 **/
	@GetMapping("/get-by/status")
	public List<LoanApplication> getByStatus(@RequestParam String status) {
		return loanApplicationService.getByStatus(status);
	}
	
	/*
	 * AIM: to fetch loan application by branchId
	 * METHOD: GET
	 * PARAM: PathVariable -> branchId
	 * RESPONSE: List<LoanApplication>
	 * PATH: /api/loanApply/get-by/branchId/{branchId}
	 */	
	@GetMapping("get-by/branchId/{branchId}")
	public List<LoanApplication> getByBranchId(@PathVariable String branchId) {
		return loanApplicationService.getByBranchId(branchId);
	}
	
	/*
	 * AIM: fetch all  
	 * METHOD: GET
	 * RESPONSE: List<LoanApplication>
	 * PATH: /api/loanApply/get-all
	 */
	@GetMapping("get-all")
	public List<LoanApplication> getAll() {
		return loanApplicationService.getAll();
	}
	
}
