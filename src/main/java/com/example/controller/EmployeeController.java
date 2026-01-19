package com.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.AuthRequest;
import com.example.config.AuthResponse;
import com.example.config.JwtService;
import com.example.entity.Employee;
import com.example.service.EmployeeDetails;
import com.example.service.EmployeeDetailsService;

@RequestMapping("/auth")
@RestController
public class EmployeeController {

	private EmployeeDetailsService employeeService;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;

	public EmployeeController(EmployeeDetailsService employeeService, AuthenticationManager authenticationManager,
			JwtService jwtService) {
		super();
		this.employeeService = employeeService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PreAuthorize("hasRole('admin')")
	@PostMapping("/addnewuser")
	public String addNewUser(@RequestBody Employee employee) {
		
		System.out.println("New User is added");
		return employeeService.addEmployee(employee);
	}

	@PreAuthorize("hasRole('admin')")
	@DeleteMapping("/employee/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		return employeeService.deleteEmployeeById(id);
	}

	@PreAuthorize("hasRole('admin')")
	@PutMapping("/employee/{id}")
	public String updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {

		return employeeService.updateEmployee(employee, id);
	}

	@PreAuthorize("hasRole('admin')")
	@GetMapping("/employee/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("/login")
	public AuthResponse authenticateAndGetTokenResponse(@RequestBody AuthRequest authRequest) {

		System.out.println("Login Controller Hit");

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (!authentication.isAuthenticated()) {
			throw new UsernameNotFoundException("invalied User request ");
		}

		EmployeeDetails userDetails = (EmployeeDetails) authentication.getPrincipal();
		String token = jwtService.generateToken(userDetails.getUsername());

		return new AuthResponse(token, userDetails.getUsername(), userDetails.getRoles());
	}

	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("Invalid user request!");
		}
	}
}
