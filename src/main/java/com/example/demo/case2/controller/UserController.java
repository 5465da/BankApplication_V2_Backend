package com.example.demo.case2.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.case2.jwt.AuthTokenFilter;
import com.example.demo.case2.jwt.JwtUtils;
import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.Payees;
import com.example.demo.case2.models.Payment_History;
import com.example.demo.case2.models.User;
import com.example.demo.case2.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/")
public class UserController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserService refUserService;

	/*
	 * Sign In Authenticate the user credentials If successful, generate a JWT token
	 * consists of the authenticate details A JSON object is created consists of
	 * token,account_status,role
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody User user) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		JSONObject obj = new JSONObject();
		obj.put("token", jwt);
		obj.put("account_status", ((User) authentication.getPrincipal()).getAccount_status());
		// obj.put("role", ((List<User>) authentication.getAuthorities()).get(0));
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
	}

	/*
	 * Register for a new account Call user service to make the registration by
	 * passing user details
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid User user) throws Exception {
		if(this.refUserService.register(user)) {
			return ResponseEntity.ok().body("Account Registered");
		}
		else return ResponseEntity.ok().body("Email already Exists");
		
	}

	/**
	 * 
	 * Get name
	 */
	@GetMapping("/customerDashboard")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<String> userAccess(HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		String name = refUserService.getName(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		return ResponseEntity.ok().body(name);
	}

	/*
	 * Card Application Apply for a specific credit card type Get JWT token from
	 * request and grab email id
	 */

	@PostMapping("/applycard")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> applyCard(@RequestBody CreditCard card, HttpServletRequest request) {

		AuthTokenFilter ATF = new AuthTokenFilter();
		if (refUserService.applyCreditCard(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)), card)) {
			return ResponseEntity.ok().body("Card Applied Successfully");
		} else
			return ResponseEntity.badRequest().body("Unable to Apply card");

	}

	@GetMapping("/creditcard")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCardDetails(HttpServletRequest request) throws Exception {

		AuthTokenFilter ATF = new AuthTokenFilter();
		List<JSONObject> list = refUserService.findCardDetails(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		if (!list.isEmpty()) {
			return ResponseEntity.ok().body(list.toString());
		} else {
			return ResponseEntity.ok().body("no data");
		}

	}

	/*
	 * Get Credit Card status
	 */
	@GetMapping("/cardstatus")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCardStatus(HttpServletRequest request) throws Exception {

		AuthTokenFilter ATF = new AuthTokenFilter();
		List<JSONObject> list = refUserService
				.getCardApplicationStatus(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		if (!list.isEmpty()) {
			return ResponseEntity.ok().body(list.toString());
		} else {
			return ResponseEntity.ok().body("no data");
		}

	}

	// Add new payees
	@PostMapping("/addpayees")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addPayees(@RequestBody Payees refPayee, HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		if (refUserService.addPayee(refPayee, jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)))) {
			return ResponseEntity.ok().body("Added Payee");
		} else {
			return ResponseEntity.badRequest().body("Unable to add Payee");
		}
	}

	// Add new mobile payees
	@GetMapping("/getmobilepayees")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getMobileayees(HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		List<JSONObject> list = refUserService.getPayeeMobile(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		if (list.isEmpty()) {
			return ResponseEntity.badRequest().body("no data");
		}
		return ResponseEntity.ok().body(list.toString());
	}

	// Get All Payees
	@GetMapping("/getpayees")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getPayees(HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		List<JSONObject> list = refUserService.getPayees(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		if (list.isEmpty()) {
			return ResponseEntity.badRequest().body("no data");
		}
		return ResponseEntity.ok().body(list.toString());
	}

	@PostMapping("/payment")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addPayment(@RequestBody Payment_History refPayee, HttpServletRequest request) {

		AuthTokenFilter ATF = new AuthTokenFilter();
		if (refUserService.pay(refPayee, jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)))) {
			return ResponseEntity.ok().body("Paid");
		}
		return ResponseEntity.badRequest().body("error occured");
	}

	@GetMapping("/check_payment")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getPaymentDetails(HttpServletRequest request) {
		AuthTokenFilter ATF = new AuthTokenFilter();
		List<JSONObject> list = refUserService
				.getPaymentHistory(jwtUtils.getUserNameFromJwtToken(ATF.parseJwt(request)));
		return ResponseEntity.ok().body(list.toString());
	}

	@PostMapping("/paymenthistory")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getPaymentCardDetails(@RequestBody CreditCard card) {
		List<JSONObject> list = refUserService.getCardPaymentHistory(card);
		return ResponseEntity.ok().body(list.toString());
	}

}
