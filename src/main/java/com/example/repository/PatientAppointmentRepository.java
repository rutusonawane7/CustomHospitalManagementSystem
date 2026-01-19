package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.PatientAppointment;

public interface PatientAppointmentRepository extends  JpaRepository<PatientAppointment, Long>{

	List<PatientAppointment> findByPatientId(Long patient_id);

}
