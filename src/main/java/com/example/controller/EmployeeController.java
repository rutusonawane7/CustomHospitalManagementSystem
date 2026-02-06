package com.example.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.AuthRequest;
import com.example.config.JwtService;
import com.example.dto.UserInformationDto;
import com.example.entity.RefreshToken;
import com.example.entity.UserInformation;
import com.example.repository.RefreshTokenRepository;
import com.example.service.RefreshTokenService;
import com.example.service.UserInformationDetails;
import com.example.service.UserInformationDetailsService;

import jakarta.validation.Valid;


@EnableMethodSecurity
@RequestMapping("/auth")
@RestController
public class EmployeeController {

	private UserInformationDetailsService employeeService;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private RefreshTokenRepository refreshTokenRepository;
	private RefreshTokenService refreshTokenService;

	public EmployeeController(UserInformationDetailsService employeeService, AuthenticationManager authenticationManager,
			JwtService jwtService, RefreshTokenRepository refreshTokenRepository,RefreshTokenService refreshTokenService) {
		super();
		this.employeeService = employeeService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenService=refreshTokenService;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addnewuser")
	public UserInformationDto addNewUser(@Valid @RequestBody UserInformation employee) {
		
		return employeeService.addEmployee(employee);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/employee/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		return employeeService.deleteEmployeeById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/employee/{id}")
	public UserInformationDto updateEmployee(@RequestBody UserInformation employee, @PathVariable Long id) {

		return employeeService.updateEmployee(employee, id);
	}
	

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/employee/{id}")
	public UserInformationDto getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    authRequest.getUsername(),
	                    authRequest.getPassword()
	            )
	    );

	    UserInformationDetails userDetails =
	            (UserInformationDetails) authentication.getPrincipal();

	    String role = userDetails.getAuthorities()
	            .iterator()
	            .next()
	            .getAuthority()
	            .replace("ROLE_", "");

	    String accessToken =
	            jwtService.generateAccessToken(
	                    userDetails.getUsername(),
	                    role
	            );

	    RefreshToken refreshToken =
	            refreshTokenService.createRefreshToken(userDetails.getId());

	    return ResponseEntity.ok(
	            Map.of(
	                    "accessToken", accessToken,
	                    "refreshToken", refreshToken.getRefreshToken(),
	                    "role", role
	            )
	    );
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {

	    String refreshToken = request.get("refreshToken");

	    if (refreshToken == null) {
	        return ResponseEntity.status(401)
	                .body(Map.of("code", "401", "message", "refresh token missing"));
	    }

	    return ResponseEntity.ok(refreshTokenService.refreshToken(refreshToken));
	}

	
	@PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }

        return refreshTokenRepository.findByRefreshToken(requestToken)
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }
}
