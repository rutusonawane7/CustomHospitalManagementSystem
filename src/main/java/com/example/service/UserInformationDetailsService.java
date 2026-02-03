package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;


import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;

@Service
public class EmployeeDetailsService implements UserDetailsService {

	private final EmployeeRepository employeeRepository;

	public EmployeeDetailsService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Employee> employee = employeeRepository.findByusername(username);

		if (employee.isEmpty()) {
			throw new UsernameNotFoundException("User not found" + username);
		}
		return new EmployeeDetails(employee.get());

	}

	public String addEmployee(Employee employee) {
		Optional<Employee> existingEmployee = employeeRepository.findByusername(employee.getUsername());
		if (existingEmployee.isPresent()) {
			throw new RuntimeException("Username is already exist");
		}

		employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
		
		String currentUser =
	            SecurityContextHolder.getContext()
	                                 .getAuthentication()
	                                 .getName();

		
		employee.setCreatedBy(currentUser);
		employee.setCreatedAt(LocalDateTime.now());
		
		
		employeeRepository.save(employee);

		return "employee added Successfully";
	}

	public String deleteEmployeeById(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with this id" + id));

		employeeRepository.deleteById(id);

		return "Employee Details Deleted Successfully !";
	}

	public String updateEmployee(Employee employee, Long id) {
		// 1. Find employee by ID
		Optional<Employee> employeeToUpdateOpt = employeeRepository.findById(id);
		if (employeeToUpdateOpt.isEmpty()) {
			throw new RuntimeException("Employee not found with Id: " + id);
		}
		Employee employeeToUpdate = employeeToUpdateOpt.get();

		// 2. Check if new username exists for another employee
		Optional<Employee> existingEmployee = employeeRepository.findByusername(employee.getUsername());
		if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(id)) {
			throw new RuntimeException("Username is already exist");
		}

		// 3. Update fields
		employeeToUpdate.setUsername(employee.getUsername());
		employeeToUpdate.setRoles(employee.getRoles());

		// 4. Encode password if provided
		if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
			employeeToUpdate.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
		}

		// 5. Save updated employee
		employeeRepository.save(employeeToUpdate);

		return "Employee updated successfully";
	}

	public Employee getEmployeeById(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id" + id));

		return employee;
	}

}
