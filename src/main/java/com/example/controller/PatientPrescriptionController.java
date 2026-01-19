package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.nio.file.Files;

import com.example.entity.PatientPrescription;
import com.example.service.PatientPrescriptionService;




@CrossOrigin(
		originPatterns = {"http://localhost:*","http://192.168.1.*:*","http://172.19.*.*:*"},
				methods= {RequestMethod.POST,RequestMethod.GET,RequestMethod.PUT,RequestMethod.OPTIONS}
	)

@RestController
public class PatientPrescriptionController {
	
	@Autowired
	PatientPrescriptionService prescriptionService;
	
	@PostMapping("/prescription/{appointmentId}")
	public PatientPrescription save(@RequestParam MultipartFile prescriptionFile, 
			
			
			@PathVariable Long appointmentId) throws IOException {
		
		System.out.println("Request from web");
		
		return prescriptionService.save(prescriptionFile, appointmentId);
		
	}
	
	/*@GetMapping("prescription/{appointmentId}")
	public ResponseEntity<Resource> getPrescriptionByAppointmentId(
	        @PathVariable Long appointmentId) {

	    Resource prescriptionImage =
	            prescriptionService.getPrescriptionByAppointmentId(appointmentId);

	    if (prescriptionImage != null) {
	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_JPEG)
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "inline; filename=\"" + prescriptionImage.getFilename() + "\"")
	                .body(prescriptionImage);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	 @GetMapping("/prescription/{appointmentId}")
	    public ResponseEntity<Resource> getPrescription(
	            @PathVariable Long appointmentId) {

	        Resource resource =
	                prescriptionService.getPrescriptionByAppointmentId(appointmentId);

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(resource);
	    }*/
	 
	 @GetMapping("/prescription/{appointmentId}")
	 public ResponseEntity<Resource> getPrescription(@PathVariable Long appointmentId) throws IOException {

	     Resource resource =
	             prescriptionService.getPrescriptionByAppointmentId(appointmentId);

	     Path path = resource.getFile().toPath();

	     // Detect actual file type (image/jpeg, image/png, application/pdf, etc.)
	     String contentType = Files.probeContentType(path);
	     if (contentType == null) {
	         contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	     }

	     return ResponseEntity.ok()
	             .contentType(MediaType.parseMediaType(contentType))
	             .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
	             .body(resource);
	 }

	


	@PutMapping("/prescription/{appointmentId}")
	public String updatePrescription(
	        @PathVariable Long appointmentId,
	        @RequestParam MultipartFile prescriptionFile) throws IOException {

	    prescriptionService.updatePrescriptionByAppointmentId(
	            appointmentId, prescriptionFile);

	    return "Prescription updated successfully";
	}
	
	@DeleteMapping("/prescription/{appointmentId}")
	public String deletePrescription(@PathVariable Long appointmentId) {
		return prescriptionService.deletePrescriptionByAppointmentId(appointmentId);
	}




	
	
}
