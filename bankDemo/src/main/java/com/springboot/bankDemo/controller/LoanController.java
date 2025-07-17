package com.springboot.bankDemo.controller;

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

import com.springboot.bankDemo.model.Loan;
import com.springboot.bankDemo.model.LoanApplication;
import com.springboot.bankDemo.service.LoanService;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {

	@Autowired
	private LoanService loanService;
	
	/*
	 * AIM: to post loan by loan application
	 * PARAM: RequestBody -> LoanApplication
	 * METHOD: POST
	 * RESPONSE: Loan
	 * PATH: /api/loan/post/{loanApplicationId}
	 * */
	@PostMapping("/post")
	public Loan postLoan(@RequestBody LoanApplication loanApplication) {
		return loanService.postLoan(loanApplication);
	}
	
	/*
	 * AIM: update loan status
	 * PARAM: PathVariable -> ID | RequestParam -> Status
	 * METHOD: PUT
	 * RESPONSE: Loan
	 * PATH: /api/loan/put/status/{id}?status=ACTIVE
	 * */
	@PutMapping("/put/status/{id}")
	public Loan putLoanStatus(@PathVariable int id, @RequestParam String status) {
		return loanService.putLoanStatus(id, status);
	}
	
	/*
	 * AIM: fetch loan by branch id
	 * PARAM: PathVariable -> BranchId
	 * METHOD: GET
	 * RESPONSE: List<Loan>
	 * PATH: /api/loan/get-by/branchId/{branchId}
	 * */
	@GetMapping("/get-by/branchId/{branchId}")
	public List<Loan> getByBrnachId(@PathVariable int branchId) {
		return loanService.getByBranchId(branchId);
	}
	
	/*
	 * AIM: fetch by loan Id
	 * PARAM: PathVariable -> Id
	 * METHOD: GET
	 * RESPONSE: Loan
	 * PATH: /api/loan/get-by/id/{id}
	 * */
	@GetMapping("/get-by/id/{id}")
	public Loan getById(@PathVariable int id) {
		return loanService.getById(id);
	}
	
	/*
	 * AIM: fetch by customer
	 * PARAM: Principal
	 * METHOD: GET
	 * RESPONSE: List<Loan>
	 * PATH: /api/loan/get-one
	 * */
	@GetMapping("/get-one")
	public List<Loan> getByCustomer(Principal principal) {
		return loanService.getByCustomer(principal.getName());
	}
	
	/*
	 * AIM: fetch all loan 
	 * METHOD: GET
	 * RESPONSE: List<Loan>
	 * PATH: /api/loan/get-all
	 * */
	@GetMapping("/get-all")
	public List<Loan> getAll() {
		return loanService.getAll();
	}
}
