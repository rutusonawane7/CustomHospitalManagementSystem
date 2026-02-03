package com.example.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Employee;

public class EmployeeDetails implements UserDetails {

private Employee employee;
	
	public EmployeeDetails(Employee employee) {
		this.employee = employee;	
	}
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simple= new SimpleGrantedAuthority(employee.getRoles());
		ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList();
		arrayList.add(simple);
		return arrayList;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return employee.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return employee.getUsername();
	}
	
	  @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
	   

	    public Employee getEmployee() {
	        return employee;
	    }
	    
	    public String getRoles() {
	        return employee.getRoles();
	    }

}
