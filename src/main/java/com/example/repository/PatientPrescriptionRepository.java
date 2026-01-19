package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.PatientAppointment;
import com.example.entity.PatientPrescription;

public interface PatientPrescriptionRepository extends JpaRepository<PatientPrescription, Long> {

	List<PatientPrescription> findByAppointment(PatientAppointment appointment);

}
