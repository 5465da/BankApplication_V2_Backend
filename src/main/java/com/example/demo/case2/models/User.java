package com.example.demo.case2.models;


import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//User Model
@Document
public class User implements UserDetails{
	
	private ObjectId id = new ObjectId();

	@NotEmpty
	private String name;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 8, max = 30)
	private String password;
	
    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;
    
    private String account_status;
    
    private String resetToken;
 
    private List roles;

    public User() {
    	this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }
    
	public ObjectId getId() {
		return id;	
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	 public void grantAuthority(String authority) {
	        if ( roles == null ) roles = new ArrayList<>();
	        roles.add(authority);
	}
	 
	 @Override
	    public List<GrantedAuthority> getAuthorities(){
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
	        return authorities;
	 }
	 
	 
	public List getRoles() {
		return roles;
	}
	
	public void setRoles(List roles) {
		this.roles = roles;
	}

	public void setName(String name) {
		this.name = name.trim().replaceAll("\\s+", " ");
	}
	
	public String getName() {
		return name;
	}
	
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	
	public String getAccount_status() {
		return account_status;
	}
	
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
}
