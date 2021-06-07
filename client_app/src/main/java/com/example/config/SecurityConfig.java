package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.service.UserDetailsServiceImpl;

@EnableWebSecurity //to enable this class as web security
@EnableGlobalMethodSecurity(securedEnabled=true)//this is also part of above 
public class SecurityConfig extends WebSecurityConfigurerAdapter{//this class will tell spring boot how to configure Spring security

	@Autowired
	private UserDetailsServiceImpl userDetailsService;//this is to create object of UserDetailsServiceImpl
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()	//to authorize request
		.antMatchers("/css/**").permitAll() //permitALl means this url can be access by any user
		.antMatchers("/js/**").permitAll()	//permitALl means this url can be access by any user
		.antMatchers("/client/help").permitAll()	//permitALl means this url can be access by any user
		.antMatchers("/client/signup").permitAll()	//permitALl means this url can be access by any user
		.antMatchers("/client/signupPage").permitAll()	//permitALl means this url can be access by any user
		.antMatchers("/client/secure/**").hasAnyRole("ADMIN","USER")	//this type of url can only be accessible to those User who have Either role as ADMIN or USER (ROLE_ADMIN or ROLE_USER)
		.and().formLogin()	//when ever login form will submit
		.loginPage("/client/login")	//login form will open from this url
		.loginProcessingUrl("/client-login")	//Thsi is the URL where SPring security will process submit data from login form
		.usernameParameter("emp_id")	//this is userId entered by user
		.passwordParameter("emp_password")	//this is password entered by user
		.defaultSuccessUrl("/client/secure/welcome")	//if login get success this url will hit means open welcome.jsp
		.and().logout()	//on click of logout button
		.logoutUrl("/client-logout")	//this is the process url of logout event
		.logoutSuccessUrl("/client/login")	//if successfull logout it will open login page
		.and().exceptionHandling()	//if somethis wrong happen while login 	
		.accessDeniedPage("/client/error");	//then error.jsp page will open
		
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{	//This is the method which will match user input userId and password with userId and password of couchbase
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());	//we are passing userDetailsService Object which will create onloading of this class . Check this UserDetailsServiceImpl to see how we r getting data from Couchbase DB
	}
}
