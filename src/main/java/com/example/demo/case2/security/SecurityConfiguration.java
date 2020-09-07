package com.example.demo.case2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.demo.case2.jwt.AuthEntryPointJwt;
import com.example.demo.case2.jwt.AuthTokenFilter;
import com.example.demo.case2.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/*
	 * Authenticate user credentials
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	/*
	 * Configuration for paths,session and authentication, filters
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers("/register","/signin","/forgetpassword","/reset/**").permitAll()
		.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	/*
	 * Password Hashing encoder for password
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
