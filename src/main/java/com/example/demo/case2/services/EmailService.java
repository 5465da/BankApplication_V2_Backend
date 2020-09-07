package com.example.demo.case2.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	public String forgetPass(String email);

	String resetPass(String token, String password);

}
