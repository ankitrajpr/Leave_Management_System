package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.example.config","com.example.controller","com.example.service"})
public class ClientAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ClientAppApplication.class, args);
	}
}
