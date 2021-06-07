package com.example.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.stereotype.Component;

import com.example.spring.bean.Employee;
import com.example.spring.service.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component	//For defining class as Controller (Autowire)
@Path("/employee")
@EnableCouchbaseRepositories(basePackages= {"com.example.spring"})
public class JaxRestController {
	

	//create object of Inteface IEmloyeeService to call EmployeeService Methods
	@Autowired
	IEmployeeService empService;

	//To create obj of Logger class to logg 
	private final Logger logger = LoggerFactory.getLogger(JaxRestController.class);
	
	//to create Object of ObjectMapper to convert Object to String 
	ObjectMapper mapper = new ObjectMapper();
	
	@GET	//for Get type of request
	@Path("/{empId}")	//hiting url path
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployee(@PathParam("empId") long empId){//Get Method Jersey API 
		System.out.println("Started to fetch All Employee");
		logger.info("getEmployee : Started");
		Employee employee;
		String result = null;
		int statusCode = 200;
		try {
			employee= empService.getEmployeeByEmpId(empId);//calling service layer method
			result = mapper.writeValueAsString(employee);
			statusCode=200;
		} catch (Exception e) {
			//code 202 means may be API is taking more time or not able toprocess request
			statusCode=500;
			System.out.println(e.getMessage());
		}
		return Response.status(statusCode).entity(result).build();
	}
	
	@GET	//for Get type of request
	@Path("/document/{docId}")	//hiting url path
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployeeDocument(@PathParam("docId") String docId){//GET Method Jersey API with Path Variable
		System.out.println("Started to fetch All Employee with doc");
		logger.info("getEmployeeDocument : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = mapper.writeValueAsString(empService.getEmployeeByDocumentId(docId));//calling service layer method
			statusCode=200;
		} catch (Exception e) {
			//code 202 means may be API is taking more time or not able toprocess request
			statusCode=500;
			System.out.println(e.getMessage());
		}
		return Response.status(statusCode).entity(result).build();
	}
	
	@POST	//for Post type of method
	@Path("/add")	//hiting url path
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEmployee(Employee emp) {//POST method API Jersey
		System.out.println("Add New Employee Started");
		logger.info("addEmployee : Started");
		int statusCode = 200;
		try {
			empService.addNewEmployee(emp);//calling service layer method
			//staus code 204 mean sucess but will not return any thing back.
			statusCode=201;
		} catch (Exception e) {
			//code 202 means may be API is taking more time or not able to process request
			//500 mean there is internal Exception in server
			statusCode=500;
			System.out.println(e.getMessage());
		}
		return Response.status(statusCode).build();
	}
	
	@GET
	@Path("/validate/{empId}")	//hiting url path
	public Response validateUser(@PathParam("empId") long empId) {//GET method API
		System.out.println("Validation Started");
		logger.info("validateUser : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "valid : "+empService.validateEmployeeByEmpId(empId);//calling service layer method
		}catch(Exception e) {
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@POST
	@Path("/delete/{empId}")	//hiting url path with path parameter. the empId will be the same value which mentioned in URL ie /delete/12345. so empId=12345
	public Response deleteUser(@PathParam("empId") long empId) {//POST MEthod API
		System.out.println("Delete Method Started");
		logger.info("deleteUser : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "deleted : "+empService.deleteEmployeeByEmpId(empId);//calling service layer method
			statusCode = 200;
		}catch(Exception e) {
			//You can implement different Exception to respond different Status code.
			//if its Couchbase API error we can throw 500 which m not handling here ...
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@PUT
	@Path("/update/{empId}")//Hitting url
	public Response updateLeave(@PathParam("empId") long empId) {//PUT method API having path variable/parameter
		System.out.println("Update Method Initialise");
		logger.info("updateLeave : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "updated : "+empService.updateEmployeeByEmpId(empId);//calling service layer method
			statusCode = 200;
		}catch(Exception e) {
			//You can implement different Exception to respond different Status code.
			//if its Couchbase API error we can throw 500 which m not handling here ...
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@PUT
	@Path("/applyLeave")//hitting url
	public Response applyLeave(Employee emp) {//PUT Method API
		System.out.println("Apply Leave Method Initialise");
		logger.info("applyLeave : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "updated : "+empService.applyLeave(emp);//calling service layer method
			statusCode = 200;
		}catch(Exception e) {
			//You can implement different Exception to respond different Status code.
			//if its Couchbase API error we can throw 500 which m not handling here ...
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@PUT
	@Path("/approveLeave")//hitting url
	public Response approveLeave(Employee emp) {//PUT API
		System.out.println("Apply Leave Method Initialise");
		logger.info("approveLeave : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "approved : "+empService.approveLeave(emp);//calling service layer method
			statusCode = 200;
		}catch(Exception e) {
			//You can implement different Exception to respond different Status code.
			//if its Couchbase API error we can throw 500 which m not handling here ...
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@PUT
	@Path("/rejectLeave")//hitting url
	public Response rejectLeave(Employee emp) {//PUT API
		System.out.println("Apply Leave Method Initialise");
		logger.info("rejectLeave : Started");
		String result = null;
		int statusCode = 200;
		try {
			result = "rejected : "+empService.rejectLeave(emp);//calling service layer method
			statusCode = 200;
		}catch(Exception e) {
			//You can implement different Exception to respond different Status code.
			//if its Couchbase API error we can throw 500 which m not handling here ...
			System.out.println(e.getMessage());
			statusCode=500;
		}
		return Response.status(statusCode).entity(result).build();	//To return proper data with status code success
	}
	
	@GET	//for Get type of request
	@Path("/bossId/{bossId}")	//hiting url path
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployee(@PathParam("jobLevel") long jobLevel, @PathParam("bossId") long bossId){//GET API 
		System.out.println("Started to fetch All Junior Emloyee");
		logger.info("getEmployee : Started");
		List<Employee> empList;
		String result = null;
		int statusCode = 200;
		try {
			empList = empService.getJuniorEmployee(bossId);//calling service layer method
			result = mapper.writeValueAsString(empList);//convert Object to String with mapper
			statusCode=200;
		} catch (Exception e) {
			//code 202 means may be API is taking more time or not able toprocess request
			statusCode=500;
			System.out.println(e.getMessage());
		}
		return Response.status(statusCode).entity(result).build();
	}
}

