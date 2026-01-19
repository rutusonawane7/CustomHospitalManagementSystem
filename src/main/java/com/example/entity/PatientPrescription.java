package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class PatientPrescription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String prescriptionFile;
	
	
	@ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private PatientAppointment appointment;

	public PatientPrescription() {
		
	}
	
	public PatientPrescription(Long id, String prescriptionFile, PatientAppointment appointment) {
		super();
		this.id = id;
		this.prescriptionFile = prescriptionFile;
		this.appointment = appointment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrescriptionFile() {
		return prescriptionFile;
	}

	public void setPrescriptionFile(String prescriptionFile) {
		this.prescriptionFile = prescriptionFile;
	}

	public PatientAppointment getAppointment() {
		return appointment;
	}

	public void setAppointment(PatientAppointment appointment) {
		this.appointment = appointment;
	}

	
}
