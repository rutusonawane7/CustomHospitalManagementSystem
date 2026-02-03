package com.example.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class PatientRegistration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Patient First Name is Blank, please Enter Patient First Name")
	private String firstName;
	
	@NotBlank(message = "Patient Last Name is Blank, please Enter Patient Last Name")
	private String lastName;
	
	@NotBlank(message = "Patient Contact Number is Blank, Please Enter Patient Contact Number")
	private String contactNumber;
	
	@NotNull(message = "Patient Date of Birth is Blank, Please Enter Patient Date of Birth")
	private LocalDate dateOfBirth;
	
	@NotBlank(message = "Patient Gender is Blank, Please Enter Patient Gender")
	private String gender;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientAppointment> appoinments;
	
	
	
	public PatientRegistration() {
		
	}

	public PatientRegistration(Long id, String firstName, String lastName, String contactNumber, LocalDate dateOfBirth,
			String gender) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	

}
