package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserInformationDto;
import com.example.entity.UserInformation;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.UserInformationRepository;

@Service
public class UserInformationDetailsService implements UserDetailsService {

	private final UserInformationRepository userInformationRepository;

    private final PasswordEncoder passwordEncoder;

	
	public UserInformationDetailsService(UserInformationRepository userInformationRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userInformationRepository = userInformationRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserInformation> userInformation = userInformationRepository.findByusername(username);

		if (userInformation.isEmpty()) {
			throw new UsernameNotFoundException("User not found " + username);
		}
		return new UserInformationDetails(userInformation.get());

	}

	public UserInformationDto mapDto(UserInformation userInformation) {
		UserInformationDto dtoObject = new UserInformationDto();
		
		dtoObject.setId(userInformation.getId());
		dtoObject.setName(userInformation.getName());
		dtoObject.setUsername(userInformation.getUsername());
		dtoObject.setPassword(userInformation.getPassword());
		dtoObject.setRoles(userInformation.getRoles());
		
		return dtoObject;
	}
	public UserInformationDto addEmployee(UserInformation employee) {
		
		Optional<UserInformation> existingEmployee = userInformationRepository.findByusername(employee.getUsername());
		if (existingEmployee.isPresent()) {
			throw new UsernameAlreadyExistsException("Username already exists");
		}

		//employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
		
        //employee.setPassword(passwordEncoder.encode(employee.getPassword()));

		employee.setPassword(passwordEncoder.encode(employee.getUsername()));

       Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserInformationDetails currentUser =
                (UserInformationDetails) authentication.getPrincipal();

		
		employee.setCreatedBy(currentUser.getId());
		employee.setCreatedAt(LocalDateTime.now());
		
		
		userInformationRepository.save(employee);

		return mapDto(employee);
	}

	public String deleteEmployeeById(Long id) {
		UserInformation employee = userInformationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with this id" + id));

		userInformationRepository.deleteById(id);

		return "Employee Details Deleted Successfully !";
	}

	public UserInformationDto updateEmployee(UserInformation employee, Long id) {
		
		Optional<UserInformation> employeeToUpdateOpt = userInformationRepository.findById(id);
		if (employeeToUpdateOpt.isEmpty()) {
			throw new ResourceNotFoundException("Employee not found with Id: " + id);
		}
		UserInformation employeeToUpdate = employeeToUpdateOpt.get();

		
		Optional<UserInformation> existingEmployee = userInformationRepository.findByusername(employee.getUsername());
		if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(id)) {
			throw new UsernameAlreadyExistsException("Username is already exist");
		}

		
		employeeToUpdate.setUsername(employee.getUsername());
		employeeToUpdate.setRoles(employee.getRoles());

		
		if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
			employeeToUpdate.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
		}

		
		userInformationRepository.save(employeeToUpdate);

		return mapDto(employeeToUpdate);
	}

	public UserInformationDto getEmployeeById(Long id) {
		UserInformation employee = userInformationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

		return mapDto(employee);
	}

}
