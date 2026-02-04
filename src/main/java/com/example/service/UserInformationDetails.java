package com.example.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.UserInformation;

public class UserInformationDetails implements UserDetails {


private final UserInformation user;

	
	public UserInformationDetails(UserInformation user) {
		super();
		this.user = user;
	}

	public Long getId() {
        return user.getId();
    }

	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(
	        new SimpleGrantedAuthority("ROLE_" + user.getRoles())
	    );
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
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
	   

	    public UserInformation getEmployee() {
	        return user;
	    }
	    
	    public String getRoles() {
	        return user.getRoles();
	    }

}
