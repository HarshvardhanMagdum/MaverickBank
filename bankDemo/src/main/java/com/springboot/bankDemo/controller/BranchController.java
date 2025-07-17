package com.springboot.bankDemo.controller;

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

import com.springboot.bankDemo.model.Branch;
import com.springboot.bankDemo.service.BranchService;

@RestController
@RequestMapping("/api/branch")
@CrossOrigin(origins = "http://localhost:5173")
public class BranchController {

	@Autowired
	private BranchService branchService;
	
	/*
	 * AIM: to insert values to branch
	 * METHOD: POST
	 * PARAM: Branch -> RequestBody
	 * RESPONSE: Branch
	 * PATH: /api/branch/post
	 * ACCESS: GM
	 * */
	@PostMapping("/post")
	public Branch postBranch(@RequestBody Branch branch) {
		return branchService.postBranch(branch);
	}
	
	/*
	 * AIM: update branch details
	 * METHOD: PUT
	 * PARAM: PathVariable -> Id, RequestBody -> Branch
	 * RESPONSE: Branch
	 * PATH: /api/branch/put/{id}
	 * */
	@PutMapping("/put/{id}")
	public Branch putBranch(@PathVariable int id, @RequestBody Branch branch) {
		return branchService.putBranch(id, branch);
	}
	
	/*
	 * AIM: fetch all branch
	 * METHOD: GET
	 * RESPONSE: List<Branch>
	 * PATH: /api/branch/get-all
	 * */
	@GetMapping("/get-all")
	public List<Branch> getAll() {
		return branchService.getAll();
	}
	
	/*
	 * AIM: fetch branch by ifscCode
	 * METHOD: GET
	 * PARAM: ifscCode -> ReqParam
	 * RESPONSE: Branch
	 * PATH: /api/branch/get/ifscCode?ifscCode= MAVK0000001 
	 * */
	@GetMapping("/get/ifscCode")
	public Branch getByIfscCode(@RequestParam String ifscCode) {
		return branchService.getByIfscCode(ifscCode);
	}
	
	/*
	 * AIM: fetch branch by id
	 * METHOD: GET
	 * PARAM: BranchId -> PathVariable
	 * RESPONSE: Branch
	 * PATH: /api/branch/get-one/{id}
	 * */
	@GetMapping("get-one/{id}")
	public Branch getById(@PathVariable int id) {
		return branchService.getById(id);
	}
}
