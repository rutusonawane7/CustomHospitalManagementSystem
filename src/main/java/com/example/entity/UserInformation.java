package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class UserInformation extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "name must not be empty")
	private String name;
	
	@NotBlank(message = "username must not be empty")
	private String username;
	
	//@NotBlank(message = "password must not be empty")
	private String password;
	
	@NotBlank(message = "roles must not be empty")
	private String roles;

	
	public UserInformation() {
		super();
	}


	public UserInformation(Long id, String username, String password, String roles, String name) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	

}
