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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankDemo.dto.ExecutiveCreateDto;
import com.springboot.bankDemo.model.Executive;
import com.springboot.bankDemo.service.ExecutiveService;

@RestController
@RequestMapping("/api/executive")
@CrossOrigin(origins = "http://localhost:5173")
public class ExecutiveController {

	@Autowired
	private ExecutiveService executiveService;
	
	/*
	 * AIM: to insert executive by branch id
	 * METHOD: POST 
	 * PARAM: ExecutiveCreateDto -> RequestBody, Pathvariable branch id
	 * RESPONSE: Executive 
	 * PATH: /api/executive/post/{branachId}
	 * ACCESS: MANAGER, ADMIN
	 */
	@PostMapping("/post/{branchId}")
	public Executive postExecutive(@PathVariable int branchId, @RequestBody ExecutiveCreateDto executiveCreateDto) {
		return executiveService.postExecutive(branchId, executiveCreateDto);
	}
	
	/*
	 * AIM: update executive by loggedIn credentials
	 * METHOD: PUT
	 * PARAM: Executive -> RequestBody, Principal -> executive-username
	 * RESPONSE: Executive 
	 * PATH: /api/executive/put
	 * ACCESS: Executive
	 */
	@PutMapping("/put")
	public Executive putExecutive(Principal principal, @RequestBody Executive executive) {
		String username = principal.getName();
		return executiveService.putExecutive(username, executive);
	}
	
	/*
	 * AIM: get executive data by logIn cred
	 * METHOD: GET
	 * PARAM: Principal -> executive-username
	 * RESPONSE: Executive 
	 * PATH: /api/executive/get-one
	 * ACCESS: Executive
	 */
	@GetMapping("/get-one")
	public Executive getByUsername(Principal principal) {
		String username = principal.getName();
		return executiveService.getByUsername(username);
	}
	
	/*
	 * AIM: get executive by branchId
	 * METHOD: GET
	 * PARAM: branchId ->PathVariable
	 * RESPONSE: List<Executive> 
	 * PATH: /api/executive/get/{branchId}
	 * ACCESS: MANAGER, GM
	 */
	@GetMapping("/get/{branchId}")
	public List<Executive> getByBranch(@PathVariable int branchId) {
		return executiveService.getByBranch(branchId);
	}
	
	/*
	 * AIM: fetch all executive
	 * METHOD: GET
	 * RESPONSE: List<Executive>
	 * PATH: /api/executive/get-all
	 * */
	@GetMapping("get-all")
	public List<Executive> getAll() {
		return executiveService.getAll();
	}
	
}
