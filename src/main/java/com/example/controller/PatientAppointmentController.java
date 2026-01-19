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

import com.example.dto.PatientAppointmentDto;
import com.example.entity.PatientAppointment;
import com.example.service.PatientAppointmentService;

@CrossOrigin(originPatterns = { "http://localhost:*", "http://192.168.1.*:*", "http://172.19.*.*:*" }, methods = {
		RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS })
@RestController
public class PatientAppointmentController {

	@Autowired
	PatientAppointmentService appointmentService;

	public static final Logger logger = LoggerFactory.getLogger(PatientAppointmentController.class);

	/*
	 * @PostMapping("/patients/{patient_id}/appointments") public PatientAppointment
	 * createPatientAppointment(@PathVariable Long patient_id, @RequestBody
	 * PatientAppointment appointment) { return
	 * appointmentService.save(patient_id,appointment);
	 * 
	 * }
	 * 
	 * @GetMapping("/patients/{patient_id}/appointments") public
	 * List<PatientAppointment> getAppointmentsByPatientId(
	 * 
	 * @PathVariable Long patient_id) {
	 * 
	 * System.out.println("Get Appointment By Patinet id"+patient_id); return
	 * appointmentService.getAppointmentsByPatientId(patient_id); }
	 */

	/*
	 * @GetMapping("/appointments/{id}") public Optional<PatientAppointment>
	 * getAppointmentByAppointmentId(@PathVariable Long id){ return
	 * appointmentService.getAppointmentByAppointmentId(id); }
	 * 
	 * @GetMapping("/appointments") public List<PatientAppointment>
	 * getAllAppointments(){ return appointmentService.getAllAppointments(); }
	 */

	@DeleteMapping("/appointments/{id}")
	public String cancelAppointment(@PathVariable Long id) {
		logger.info("DELETE Api is colled");
		return appointmentService.cancelAppointmentById(id);
	}

	/*
	 * @PutMapping("/appointments/{id}") public String
	 * updateAppointment(@PathVariable Long id, @RequestBody PatientAppointment
	 * appointment) {
	 * 
	 * return appointmentService.updateAppointment(id,appointment); }
	 * 
	 * /*
	 * 
	 * Curd by using dto
	 * 
	 */

	@PostMapping("/patients/{patient_id}/appointments")
	public PatientAppointmentDto createPatientAppointment(@PathVariable Long patient_id,
			@RequestBody PatientAppointment appointment) {

		logger.info("POST /patients/{}/appointments API called to create appointment", patient_id);

		return appointmentService.createPatientAppointment(patient_id, appointment);
	}

	@GetMapping("/patients/{patient_id}/appointments")
	public List<PatientAppointmentDto> getAppointmentsByPatientId(@PathVariable Long patient_id) {
		{
			System.out.println("Get Appointment By Patinet id" + patient_id);
			return appointmentService.getAppointmentsByPatientId(patient_id);
		}
	}

	@GetMapping("/appointments/{id}")
	public PatientAppointmentDto getAppointmentByAppointmentId(@PathVariable Long id) {
		return appointmentService.getAppointmentByAppointmentId(id);
	}

	@GetMapping("/appointments")
	public List<PatientAppointmentDto> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

	@PutMapping("/appointments/{id}")
	public PatientAppointmentDto updateAppointment(@PathVariable Long id, @RequestBody PatientAppointment appointment) {
		return appointmentService.updateAppointment(id, appointment);
	}
}
