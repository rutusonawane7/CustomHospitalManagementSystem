package com.example.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.controller.PatientRegistrationController;
import com.example.dto.PatientRegistrationDto;
import com.example.entity.PatientRegistration;
import com.example.repository.PatientRegistrationRepository;

@Service
public class PatientRegistrationService {

	@Autowired
	PatientRegistrationRepository patientRepository;

	public static final Logger logger = LoggerFactory.getLogger(PatientRegistrationService.class);

	/*
	 * public PatientRegistration save(PatientRegistration patient) {
	 * 
	 * return patientRepository.save(patient); }
	 * 
	 * public PatientRegistration findById(Long id) { return
	 * patientRepository.findById(id) .orElseThrow(() -> new
	 * RuntimeException("this id " + id + " is not found in the database ")); }
	 */

	public String deleteById(Long id) {
		patientRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("this id " + id + " is not found in the database "));
		patientRepository.deleteById(id);
		return "Patient Deleted Successfully ";

	}

	/*
	 * public PatientRegistration updatePatient(Long id, PatientRegistration
	 * patient) { PatientRegistration existingPatient =
	 * patientRepository.findById(id) .orElseThrow(() -> new
	 * RuntimeException("Patient with " + id + " not found"));
	 * 
	 * existingPatient.setId(patient.getId());
	 * existingPatient.setFirstName(patient.getFirstName());
	 * existingPatient.setLastName(patient.getLastName());
	 * existingPatient.setContactNumber(patient.getContactNumber());
	 * existingPatient.setGender(patient.getGender());
	 * existingPatient.setDateOfBirth(patient.getDateOfBirth());
	 * 
	 * return patientRepository.save(existingPatient); }
	 * 
	 * public List<PatientRegistration> getAllPatients() {
	 * 
	 * return patientRepository.findAll(); }
	 */

	/*
	 * 
	 * Service Layer By using Dto
	 * 
	 */

	public PatientRegistrationDto mapDto(PatientRegistration patient) {
		PatientRegistrationDto dtoObject = new PatientRegistrationDto();

		dtoObject.setId(patient.getId());
		dtoObject.setFirstName(patient.getFirstName());
		dtoObject.setLastName(patient.getLastName());
		dtoObject.setContactNumber(patient.getContactNumber());
		dtoObject.setGender(patient.getGender());
		dtoObject.setDateOfBirth(patient.getDateOfBirth());

		return dtoObject;

	}

	public PatientRegistrationDto createPatients(PatientRegistration patient) {
		logger.info("Starting patient creation process");
		PatientRegistration savePatient = patientRepository.save(patient);
		logger.info("Patient successfully saved with ID: {}", savePatient.getId());
		return mapDto(savePatient);
	}

	public PatientRegistrationDto findById(Long id) {
		logger.info("Fetching patient with ID: {}", id);

		PatientRegistration patient = patientRepository.findById(id).orElseThrow(() -> {
			logger.warn("Patient not found with ID: {}", id);
			return new RuntimeException("Patient not found");
		});

		logger.info("Patient found with ID: {}", id);
		return mapDto(patient);
	}

	public PatientRegistrationDto updatePatient(Long id, PatientRegistration patient) {
		logger.info("Starting update process for patient ID: {}", id);
		PatientRegistration existingPatient = patientRepository.findById(id)
				.orElseThrow(() -> 
				{
					logger.warn("Patient not found with ID: {}", id);
				return new RuntimeException("Patient not found");});

	    logger.info("Updating patient details for ID: {}", id);

		// existingPatient.setId(patient.getId());
		existingPatient.setFirstName(patient.getFirstName());
		existingPatient.setLastName(patient.getLastName());
		existingPatient.setContactNumber(patient.getContactNumber());
		existingPatient.setGender(patient.getGender());
		existingPatient.setDateOfBirth(patient.getDateOfBirth());

		patientRepository.save(existingPatient);
		logger.info("Patient updated successfully with ID: {}", id);

		return mapDto(existingPatient);
	}

	public List<PatientRegistrationDto> getAllPatients() {
		logger.info("Fetching all patients from database");

		List<PatientRegistration> patients = patientRepository.findAll();
		logger.info("Total patients fetched: {}", patients.size());
		return patients.stream().map(this::mapDto).toList();

	}

}
