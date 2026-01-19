package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PatientRegistrationDto;
import com.example.entity.PatientRegistration;
import com.example.service.PatientRegistrationService;

@CrossOrigin(originPatterns = { "http://localhost:*", "http://192.168.1.*:*", "http://172.19.*.*:*" }, methods = {
		RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS })
@RestController
public class PatientRegistrationController {

	@Autowired
	PatientRegistrationService patientService;

	public static final Logger logger = LoggerFactory.getLogger(PatientRegistrationController.class);
	/*@PostMapping("/patients")
	public PatientRegistration createPatient(@RequestBody PatientRegistration patient) {
		return patientService.save(patient);
	}
	
	@GetMapping("/patients/{id}")
	public PatientRegistration getPatientById(@PathVariable Long id) {
		return patientService.findById(id);
	}*/

	@DeleteMapping("/patients/{id}")
	public String deletePatientById(@PathVariable Long id) {
		logger.info("DELETE/patients/{} API called", id);
		return patientService.deleteById(id);
	}

	/*@PutMapping("/patients/{id}")
	public PatientRegistration updatePatient(@PathVariable Long id, @RequestBody PatientRegistration patient) {
		return patientService.updatePatient(id, patient);
	}

	@GetMapping("/patients")
	public List<PatientRegistration> getAllPatients() {
		return patientService.getAllPatients();


	}*/
	
	/*
	 * 
	 * CURD Operations By using Data Transfer Object 
	 * 
	 */
	@PostMapping("/patients")
	public PatientRegistrationDto createPatients(@RequestBody PatientRegistration patient) {
		 logger.info("POST API called to create patient");
		return patientService.createPatients(patient);
	}
	
	@GetMapping("/patients/{id}")
	public PatientRegistrationDto getPatientById(@PathVariable Long id) {
		logger.info("GET /patients/{} API called", id);
		return patientService.findById(id);
	}
	
	@PutMapping("/patients/{id}")
	public PatientRegistrationDto updatePatient(@PathVariable Long id, @RequestBody PatientRegistration patient) {
		logger.info("PUT /patients/{} API called for update", id);

		return patientService.updatePatient(id, patient);
	}
	
	@GetMapping("/patients")
	public List<PatientRegistrationDto> getAllPatients() {
		logger.info("GET /patients API called to fetch all patients");
		return patientService.getAllPatients();
		
	}

}
