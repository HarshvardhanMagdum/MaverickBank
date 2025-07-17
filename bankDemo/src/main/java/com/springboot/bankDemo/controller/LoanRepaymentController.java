package com.springboot.bankDemo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.model.LoanRepayment;
import com.springboot.bankDemo.service.LoanRepaymentService;

@RestController
@RequestMapping("/api/loanRepay")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanRepaymentController {

	@Autowired
	private LoanRepaymentService loanRepaymentService;
	
	/*
	 * AIM: post loan repayment with loan id and amount
	 * PARAM: PathVariable -> LoanId | RequestParam -> amount
	 * METHOD: POST
	 * RESPONSE: LoanRepayment
	 * PATH: /api/loanRepay/post/{loanId}?amount=10000
	 * */
	@PostMapping("/post/{loanId}")
	public LoanRepayment postLoanRepayment(@PathVariable int loanId, @RequestParam BigDecimal amount) {
		return loanRepaymentService.postLoanRepayment(loanId, amount);
	}
	
	/*
	 * AIM: fetch loan repayment by loan Id
	 * PARAM: PathVariable -> loanId
	 * METHOD: GET
	 * RESPONSE: List<LoanRepayment>
	 * PATH: /api/loanRepay/get-by/loanId/{loanId}
	 * */
	@GetMapping("/get-by/loanId/{loanId}")
	public List<LoanRepayment> getByLoanId(@PathVariable int loanId) {
		return loanRepaymentService.getByLoanId(loanId);
	}
}
