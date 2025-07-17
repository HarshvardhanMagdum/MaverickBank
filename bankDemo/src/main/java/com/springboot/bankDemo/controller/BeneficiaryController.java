package com.springboot.bankDemo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.exception.ResourceNotFoundException;
import com.springboot.bankDemo.model.Beneficiary;
import com.springboot.bankDemo.service.BeneficiaryService;

@RestController
@RequestMapping("/api/beneficiary")
@CrossOrigin(origins = "http://localhost:5173")
public class BeneficiaryController {

	@Autowired
	private BeneficiaryService beneficiaryService;
	
	/*
	 * AIM: to insert values into beneficiary by user log cred
	 * METHOD: POST
	 * PARAM: Beneficiary -> RequestBody, Principal
	 * RESPONSE: Beneficiary
	 * PATH: /api/beneficiary/post
	 * */
	@PostMapping("/post")
	public ResponseEntity<?> postBeneficiary(Principal principal, @RequestBody Beneficiary beneficiary) {
		String username = principal.getName();
		return ResponseEntity.status(HttpStatus.OK).body(beneficiaryService.postBeneficiary(username, beneficiary));
	}
	
	/*
	 * AIM: update values of beneficiary
	 * METHOD: PUT
	 * PARAM: Beneficiary -> RequestBody, 	beneficiaryId,CustomerId -> PathVariable
	 * RESPONSE: Beneficiary
	 * PATH: /api/beneficiary/put/{id}/{customerId}
	 * */
	@PutMapping("/put/{id}/{customerId}")
	public ResponseEntity<?> putBeneficiary(@PathVariable int id, @PathVariable int customerId, 
															@RequestBody Beneficiary beneficiary) {
		return ResponseEntity.status(HttpStatus.OK).body(beneficiaryService.putBeneficiary(id, customerId, beneficiary));
	}
	
	/*
	 * AIM: fetch beneficiary by customer log cred
	 * METHOD: GET
	 * PARAM: Principal
	 * RESPONSE: List<Beneficiary>
	 * PATH: /api/beneficiary/get
	 * */
	@GetMapping("/get")
	public List<Beneficiary> getByCustomerUsername(Principal principal) throws ResourceNotFoundException {
		String username = principal.getName();
		return beneficiaryService.getByCustomerUsername(username);
	}
	
	/*
	 * AIM: fetch beneficiary by id
	 * METHOD: GET
	 * PARAM: Id -> PathVariable
	 * RESPONSE: Beneficiary
	 * PATH: /api/beneficiary/get-one/{id}
	 * */
	@GetMapping("/get-one/{id}")
	public Beneficiary getById(@PathVariable int id) {
		return beneficiaryService.getById(id);
	}
	
	/*
	 * AIM: delete beneficiary by id
	 * METHOD: DELETE
	 * PARAM: PathVaraible -> ID
	 * PATH: /api/beneficiary/delete/{id}
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable int id) {
		beneficiaryService.deleteById(id);
	}
}
