package com.example.demo.case2.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.case2.jwt.JwtUtils;
import com.example.demo.case2.models.User;
import com.example.demo.case2.repository.UserRepository;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${bankapp.apiKey}")
	private String apiKey;

	@Value("${bank.domain}")
	private String domain;

	@Autowired
	private UserRepository refUserRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * Forget password
	 * Locate if user email is located in the database 
	 * if present generate a reset token using jwt
	 * lastly send email
	 */
	@Override
	public String forgetPass(String email) {
		Optional<User> users = this.refUserRepository.findByEmail(email);
		if (users.isPresent()) {
			try {
				String token = jwtUtils.generateResetToken(email);
				User userUpdate = users.get();
				userUpdate.setResetToken(token);
				refUserRepository.save(userUpdate);
				sendSimpleMessage(apiKey, domain, email, token);
				return "Email Sent";
			} 
			catch (Exception e) {
				return "Error";
			}

		} else {
			return "Error don't exists!";
		}
	}

	/*
	 * Send Forgetpassword email to reset password
	 */
	public static void sendSimpleMessage(String key, String domainUrl, String email, String token) {
		Configuration configuration = new Configuration().domain(domainUrl).apiKey(key).from("Optimum DigiBank",
				"optimum@digibank.com");

		Mail.using(configuration).to(email).subject("Reset Password").text("To reset your password")
				.html("http://localhost:3000/reset/" + token).build().send();
	}

	
	/*
	 * 
	 * Reset Password
	 * Locate if reset Token found in any of the user in database
	 * If present, validate the jwt token
	 * If found using a old password is will return same old password
	 * If not encrypt the new password and save in database
	 */
	@Override
	public String resetPass(String token, String password) {
		Optional<User> resetToken = this.refUserRepository.findByresetToken(token);

		if (jwtUtils.validateJwtToken(token)) {
			if (resetToken.isPresent()) {
				User updatePass = resetToken.get();
				boolean result = passwordEncoder.matches(password, updatePass.getPassword());

				if (!result) {
					updatePass.setResetToken("");
					updatePass.setPassword(passwordEncoder.encode(password));
					refUserRepository.save(updatePass);
					return "ok";
				}
				return "Same Old Password";
			}
			return "Not Email exists";
		}
		return "Invalid Token";
	}

}
