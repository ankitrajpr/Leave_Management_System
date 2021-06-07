package com.example.rest;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component	//For declearing class as controller and will use as config. It is java configuration of Controller. 
public class JerseyConfig extends ResourceConfig {

	
	public JerseyConfig() {
		register(JaxRestController.class);	//defined which class is controller class.
	}
	
	
}
