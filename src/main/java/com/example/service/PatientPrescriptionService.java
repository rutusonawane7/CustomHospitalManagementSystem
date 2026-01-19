package com.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.PatientAppointment;
import com.example.entity.PatientPrescription;
import com.example.repository.PatientAppointmentRepository;
import com.example.repository.PatientPrescriptionRepository;



@Service
public class PatientPrescriptionService {

	@Autowired
	PatientPrescriptionRepository prescriptionRepository;
	
	@Autowired
	PatientAppointmentRepository appointmentRepository;
	
	
	
	private static final String PRESCRIPTION = "PrescriptionFile";
	
	
	
	/*public PatientPrescription save(MultipartFile prescriptionFile, Long appointmentId)
	        throws IOException {

	    PatientAppointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new RuntimeException("Appointment with this id is not found"));

	    String projectRoot = System.getProperty("user.dir");
	    String fileDirectory = projectRoot + File.separator + PRESCRIPTION;

	    File dir = new File(fileDirectory);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    String fileName = System.currentTimeMillis() + "_" + prescriptionFile.getOriginalFilename();
	    String filePath = fileDirectory + File.separator + fileName;

	    File destinationFile = new File(filePath);
	    prescriptionFile.transferTo(destinationFile);

	    PatientPrescription prescription = new PatientPrescription();
	    prescription.setPrescriptionFile(PRESCRIPTION + "/" +  prescriptionFile.getOriginalFilename() );
	    prescription.setAppointment(appointment);
	    
	    
	    
	    PatientPrescription patientPriscription = prescriptionRepository.save(prescription);


	    return patientPriscription;
	}*/
	public PatientPrescription save(MultipartFile prescriptionFile, Long appointmentId)
        throws IOException {

    PatientAppointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment with this id is not found"));

    String projectRoot = System.getProperty("user.dir");
    String fileDirectory = projectRoot + File.separator + PRESCRIPTION;

    File dir = new File(fileDirectory);
    if (!dir.exists()) {
        dir.mkdirs();
    }

    String fileName = System.currentTimeMillis() + "_" + prescriptionFile.getOriginalFilename();
    String fullFilePath = fileDirectory + File.separator + fileName;

    prescriptionFile.transferTo(new File(fullFilePath));

    PatientPrescription prescription = new PatientPrescription();
    
    // ✅ STORE ONLY FOLDER + FILENAME
    prescription.setPrescriptionFile(PRESCRIPTION + "/" + fileName);
    prescription.setAppointment(appointment);

    return prescriptionRepository.save(prescription);
}

	
	public Resource getPrescriptionByAppointmentId1(Long appointmentId) {

        PatientAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        List<PatientPrescription> prescriptions =
                prescriptionRepository.findByAppointment(appointment);

        if (prescriptions.isEmpty()) {
            throw new RuntimeException("No prescription found for this appointment");
        }

        PatientPrescription latestPrescription =
                prescriptions.get(prescriptions.size() - 1);

        // DB value: PrescriptionFile/filename.jpg
        String relativePath = latestPrescription.getPrescriptionFile();

        // Rebuild absolute path
        Path absolutePath = Paths.get(System.getProperty("user.dir"))
                .resolve(relativePath)
                .normalize();

        if (!Files.exists(absolutePath) || !Files.isReadable(absolutePath)) {
            throw new RuntimeException("Prescription file not found on server");
        }

        return new FileSystemResource(absolutePath);
    }

	
	/*public Resource getPrescriptionByAppointmentId(Long appointmentId) {

	    PatientAppointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new RuntimeException("Appointment not found"));

	    List<PatientPrescription> prescriptions =
	            prescriptionRepository.findByAppointment(appointment);

	    if (prescriptions.isEmpty()) {
	        throw new RuntimeException("No prescription found");
	    }

	    PatientPrescription latestPrescription =
	            prescriptions.get(prescriptions.size() - 1);

	    String filePath = latestPrescription.getPrescriptionFile();

	    try {
	        Path path = Paths.get(filePath);
	        return new UrlResource(path.toUri());
	    } catch (Exception e) {
	        throw new RuntimeException("Error retrieving prescription file", e);
	    }
	}*/
	
	public Resource getPrescriptionByAppointmentId(Long appointmentId) {

        PatientAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        List<PatientPrescription> prescriptions =
                prescriptionRepository.findByAppointment(appointment);

        if (prescriptions.isEmpty()) {
            throw new RuntimeException("No prescription found for this appointment");
        }

        // Get latest prescription
        PatientPrescription latestPrescription =
                prescriptions.get(prescriptions.size() - 1);

        String filePath = latestPrescription.getPrescriptionFile();
        Path path = Paths.get(filePath);

        // IMPORTANT: validate file existence
        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new RuntimeException("Prescription file not found on server");
        }

        return new FileSystemResource(path);
    }


	public void updatePrescriptionByAppointmentId(
	        Long appointmentId,
	        MultipartFile newFile) throws IOException {

	    PatientAppointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new RuntimeException("Appointment not found"));

	    List<PatientPrescription> prescriptions =
	            prescriptionRepository.findByAppointment(appointment);

	    if (prescriptions.isEmpty()) {
	        throw new RuntimeException("No prescription found for this appointment");
	    }

	   
	    PatientPrescription prescription =
	            prescriptions.get(prescriptions.size() - 1);

	    
	    String oldFilePath = prescription.getPrescriptionFile();
	    if (oldFilePath != null) {
	        File oldFile = new File(oldFilePath);
	        if (oldFile.exists()) {
	            oldFile.delete();
	        }
	    }

	    
	    String uploadDir = "PrescriptionFile";
	    File dir = new File(uploadDir);
	    if (!dir.exists()) dir.mkdirs();

	    String newFileName =
	            System.currentTimeMillis() + "_" + newFile.getOriginalFilename();

	    String newFilePath =
	            dir.getAbsolutePath() + File.separator + newFileName;

	    newFile.transferTo(new File(newFilePath));

	    
	    prescription.setPrescriptionFile(newFilePath);
	    prescriptionRepository.save(prescription);
	}

	public String deletePrescriptionByAppointmentId(Long appointmentId) {
		
		PatientAppointment appointment = appointmentRepository.findById(appointmentId).
				orElseThrow(()-> new RuntimeException("Prescription is not found "));


		List<PatientPrescription> prescriptions =
	            prescriptionRepository.findByAppointment(appointment);

	    if (prescriptions.isEmpty()) {
	        throw new RuntimeException("No prescription found for this appointment");
	    }

	    
	    PatientPrescription prescription =
	            prescriptions.get(prescriptions.size() - 1);

	    
	    String filePath = prescription.getPrescriptionFile();
	    if (filePath != null) {
	        File file = new File(filePath);
	        if (file.exists()) {
	            file.delete();
	        }
	        
	    }

	    
	    prescriptionRepository.delete(prescription);
		return "Prescription Deleted Successfully.";
				
	}
	


	

}
