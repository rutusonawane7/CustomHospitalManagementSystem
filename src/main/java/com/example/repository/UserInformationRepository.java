package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.UserInformation;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long>{
	
	Optional<UserInformation> findByusername( String username);

}
