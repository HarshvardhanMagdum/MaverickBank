package com.springboot.bankDemo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.springboot.bankDemo.model.User;
import com.springboot.bankDemo.service.UserService;
import com.springboot.bankDemo.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;
	
	/*
	 * AIM: insert user in db with password encrypted
	 * METHOD: POST
	 * PARAM: User -> RequestBody
	 * RESPONSE: User
	 * PATH: /api/user/signup
	 * */
	@PostMapping("/signup")
	public User signUp(@RequestBody User user) {
		return userService.signUp(user);
	}
	
	/*
	 * AIM: update user status
	 * METHOD: PUT
	 * PARAM: User -> RequestParam, ID -> PathVariable
	 * RESPONSE: User
	 * PATH: /api/user/put/status/{id}?status=ACTIVE
	 * */
	@PutMapping("/put/status/{id}")
	public User putUserStatus(@PathVariable int id, @RequestParam String status) {
		return userService.putUserStatus(id, status);
	}
	
	/*
	 * AIM: get all user
	 * METHOD: GET
	 * RESPONSE: List<User>
	 * PATH: /api/user/get/all
	 * */
	@GetMapping("/get/all")
	public List<User> getAll() {
		return userService.getAll();
	}
	
	/*
	 * AIM: get token by username
	 * METHOD: GET
	 * PARAM: Principal
	 * RESPONSE: Token
	 * PATH: /api/user/token
	 * */
	@GetMapping("/token")
	public ResponseEntity<?> getToken(Principal principal) {
		try {
			String token = jwtUtil.createToken(principal.getName());
			Map<String, Object> map = new HashMap<>();
			map.put("token", token);
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	
	/*
	 * AIM: get details by username
	 * METHOD: GET
	 * PARAM: Principal
	 * RESPONSE: Object
	 * PATH: /api/user/details
	 * */
	@GetMapping("/details")
	public Object getLoggedInUserDetals(Principal principal) {
		String username = principal.getName();
		return userService.getUserInfo(username);
	}
}