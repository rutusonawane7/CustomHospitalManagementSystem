package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.PatientAppointmentDto;
import com.example.entity.PatientAppointment;
import com.example.entity.PatientRegistration;
import com.example.repository.PatientAppointmentRepository;
import com.example.repository.PatientRegistrationRepository;

@Service
public class PatientAppointmentService {

	@Autowired
	PatientAppointmentRepository appointmentRepository;

	@Autowired
	PatientRegistrationRepository patientRepository;

	/*
	 * public PatientAppointment save(Long patient_id, PatientAppointment
	 * appointment) { PatientRegistration patient =
	 * patientRepository.findById(patient_id) .orElseThrow(() -> new
	 * RuntimeException("this id is not found"));
	 * 
	 * appointment.setPatient(patient);
	 * 
	 * return appointmentRepository.save(appointment); }
	 */

	/*public List<PatientAppointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	public String updateAppointment(Long id, PatientAppointment appointment) {
		PatientAppointment existingAppoitnment = appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Appointment is not found"));

		existingAppoitnment.setAppointmentDate(appointment.getAppointmentDate());
		existingAppoitnment.setRemark(appointment.getRemark());
		appointmentRepository.save(existingAppoitnment);

		return "Appointment Updated Successfully ";
	}*/

	public String cancelAppointmentById(Long id) {
		PatientAppointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("This is not found."));
		appointmentRepository.deleteById(id);
		return ("Appointment Cancle Successfully ");
	}

	/*
	 * public List<PatientAppointment> getAppointmentsByPatientId(Long patient_id) {
	 * System.out.println("2 Get appointment by patient id"+patient_id);
	 * List<PatientAppointment> data =
	 * appointmentRepository.findByPatientId(patient_id);
	 * 
	 * System.out.println("2 Get appointment by patient id"+data.size());
	 * 
	 * return data; }
	 */

	/*
	 * public Optional<PatientAppointment> getAppointmentByAppointmentId(Long id) {
	 * return appointmentRepository.findById(id);
	 * 
	 * }
	 * 
	 * 
	 * /*
	 * 
	 * Curd by using dto
	 * 
	 */
	public PatientAppointmentDto mapToDto(PatientAppointment appointment) {
		PatientAppointmentDto dto = new PatientAppointmentDto();
		dto.setId(appointment.getId());
		dto.setAppointmentDate(appointment.getAppointmentDate());
		dto.setRemark(appointment.getRemark());
		/// dto.setPatientId(appointment.getPatientId());
		dto.setPatientId(appointment.getPatient().getId());
		return dto;
	}

	public PatientAppointmentDto createPatientAppointment(Long patient_id, PatientAppointment appointment) {
		PatientRegistration patient = patientRepository.findById(patient_id)
				.orElseThrow(() -> new RuntimeException("Patient Not found"));

		appointment.setPatient(patient);
		appointmentRepository.save(appointment);
		return mapToDto(appointment);
	}

	public List<PatientAppointmentDto> getAppointmentsByPatientId(Long patient_id) {
		PatientRegistration patient = patientRepository.findById(patient_id)
				.orElseThrow(() -> new RuntimeException("Patient not found"));

		List<PatientAppointment> appointmentList = appointmentRepository.findByPatientId(patient_id);
		return appointmentList.stream().map(this::mapToDto).toList();
	}

	public PatientAppointmentDto getAppointmentByAppointmentId(Long id) {
		PatientAppointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));

		return mapToDto(appointment);
	}

	public List<PatientAppointmentDto> getAllAppointments() {
		List<PatientAppointment> appointmentList = appointmentRepository.findAll();
		return appointmentList.stream().map(this::mapToDto).toList();
	}

	public PatientAppointmentDto updateAppointment(Long id, PatientAppointment appointment) {
		PatientAppointment existingAppointment = appointmentRepository.findById(id).
				orElseThrow(()-> new RuntimeException("Appointment Not Found"));
		existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
		existingAppointment.setRemark(appointment.getRemark());
		
		appointmentRepository.save(existingAppointment);
		return mapToDto(existingAppointment);
	}

}
