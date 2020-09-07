package com.example.demo.case2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.case2.models.User;
import com.example.demo.case2.services.EmailService;

@RestController
@CrossOrigin
@RequestMapping
public class EmailController {

	@Autowired
	EmailService emailService;

	/*
	 * Forget password
	 * Call email service to send reset link
	 */
	@PostMapping("/forgetpassword")
	public ResponseEntity<?> forgetPassword(@RequestBody User user) {
		return ResponseEntity.ok().body(emailService.forgetPass(user.getEmail()));
	}

	
	/*
	 * Reset Password
	 * Call email service to reset password by passing JWT token and the new given password
	 */
	
	@PostMapping("/reset/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody User user) {
		String msg = emailService.resetPass(token, user.getPassword());
		switch (msg) {
		case "ok":
			return ResponseEntity.ok().body("Password Reset");
		case "Same Old Password":
			return ResponseEntity.ok().body("OldPass");
		default:
			return ResponseEntity.badRequest().body("Error");
		}

	}

}
