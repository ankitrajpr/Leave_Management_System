package com.example.chetak;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.rest","com.example.spring.service"})	//Which package need too scan for controller and other components like service and Repository
public class LeaveApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(LeaveApplication.class, args);
	}

}
