package com.example.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PatientAppointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Appointment Date is Blank, Please Enter Valid Date")
	private LocalDate appointmentDate;
	
	
	private String remark;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "patient_id") 
    private PatientRegistration patient;
	
	@JsonIgnore
	@OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<PatientPrescription> prescriptions;

	
	
	public PatientAppointment() {
		
	}

	public PatientAppointment(Long id, LocalDate appointmentDate, String remark,PatientRegistration patient, List<PatientPrescription> prescriptions ) {
		super();
		this.id = id;
		this.appointmentDate = appointmentDate;
		this.remark = remark;
		this.patient = patient;
		this.prescriptions = prescriptions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public PatientRegistration getPatient() {
	    return patient;
	}

	public void setPatient(PatientRegistration patient) {
	    this.patient = patient;
	}

	public List<PatientPrescription> getPrescriptions() {
		return prescriptions;
		       
	}

	public void setPrescriptions(List<PatientPrescription> prescriptions) {
		this.prescriptions = prescriptions;
	}
 
	

}
