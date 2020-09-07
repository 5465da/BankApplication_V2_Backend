package com.example.demo.case2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.case2.jwt.AuthTokenFilter;
import com.example.demo.case2.jwt.JwtUtils;
import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.User;
import com.example.demo.case2.services.AdminService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/adminDashboard")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

	@Autowired
	private AdminService refAdminService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> adminDashboard(HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		String name = refAdminService.getName(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		return ResponseEntity.ok().body(name);
	}


	@PostMapping("/updateStatus")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> register(@RequestBody User user) {
		if(refAdminService.updateUserStatus(user.getId(), user.getAccount_status())) {
			return ResponseEntity.ok().body("Success");
		}
		 return ResponseEntity.badRequest().body("Failed to update ID not found" + user.getId());
	}
	
	@PostMapping("/updateCard")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> updateCardApplication(@RequestBody CreditCard card) {
		if(refAdminService.updateCardApp(card)) {
			return ResponseEntity.ok().body("Success");
		}
		 return ResponseEntity.badRequest().body("Fail");
	}
	
	
	@GetMapping("/pendingcards")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getAllPendingCards() {
		return ResponseEntity.ok().body(refAdminService.getPendingCards().toString());
	}
	
	
	@GetMapping("/pendingAcc")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getAllPendingUser() {
		return ResponseEntity.ok().body(refAdminService.getPendingUser().toString());
	}
	
	
	@GetMapping("/users/active")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> activeUser() {
		return ResponseEntity.ok().body(refAdminService.getActiveUser());
	}
	
	@GetMapping("/creditcards")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getAllCardHolder() {
		return ResponseEntity.ok().body(refAdminService.getCustomerCardStatus().toString());
	}
	
}
