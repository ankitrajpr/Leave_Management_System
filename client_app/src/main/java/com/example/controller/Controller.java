package com.example.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.bean.AjaxResponseBody;
import com.example.bean.Employee;
import com.example.bean.MailBean;
import com.example.service.IEmployeeService;

@Component//to define it as component class from where spring boot can scan and find method.
@RequestMapping(value="/client")//first string which will be mapped is /client
public class Controller {

	@Autowired//autometically create a object of IEmployeeService Class
	IEmployeeService empService;
	
	//To store all log data we need obj of Logger class.
	private final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@GetMapping("/help")//hitting url of GET Type
	public ModelAndView documnet() {
		logger.info("Help : Started.");
		return new ModelAndView("help");//return help which will suffix with .jsp and prefix with WEB-INF/jsp/ so final will be WEB-INF/jsp/help.jsp
	}
	
	@GetMapping("/login")//when u will hit /client/login it will come here
	public ModelAndView login() {
		logger.info("Login method at Controller");
		return new ModelAndView("login");//will return after prefix and suffix as /WEB-INF/jsp/login.jsp
	}
	
	@GetMapping("/secure/welcome")//hitting url of GET type
	public ModelAndView welcome(Authentication authentication/*This is Class of Spring security which store logged in user details*/) {
		logger.info("Welcome method at Controller");
		ModelAndView m = new ModelAndView("welcome");//create model which view name is welcome //WEB-INF/jsp/welcome.jsp
		System.out.println("Welcome");
		logger.info("Setting data from authentication principal which store all info while csrf login.");
		UserDetails userDetail = (UserDetails)authentication.getPrincipal();//geting data from spring security class stored data
		long empId = Long.parseLong(userDetail.getUsername());//We have EmplId as username so it is String value and can easily be converted to long.
		logger.info("Calling getEmployee to fetch all other data of loggedin user.");
		Employee emp = empService.getEmployeeData(empId);//calling service layer method to get employee object
		logger.info("Calling getJuniorEmployee to fetch all employee who are under him 2 level down for loggedin user.");
		List<Employee> employeeList = empService.getJuniorEmployee(Long.parseLong(emp.getEmpId()));//calling service layer method to get list of employee object
		logger.info("Adding data to model and view.");
		m.addObject("employee", emp);//adding employee object and employee list object in model which can be use on welcome.jsp page
		if(employeeList.size()>0)
			m.addObject("employeeList", employeeList);//adding to model so that we can use it on welcome.jsp page
		return m;
	}
	
	@GetMapping("/error")//hitting url /client/error GET
	public ModelAndView error() {
		System.out.println("Error");
		logger.info("Error at Controller");
		return new ModelAndView("error");//will return /WEB-INF/jsp/error.jsp
	}
	
	@GetMapping("/signupPage")//hitting url /client/signupPage GET
	public ModelAndView signupPage() {
		logger.info("Starting signup page");
		System.out.println("Signup Page");
		ModelAndView m = new ModelAndView("signup");//return /WEB-INF/jsp/signup.jsp
		return m;
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST) //when signup form submit it will come here post
	public @ResponseBody AjaxResponseBody signup(@RequestBody Employee emp) {//AjaxResponseBody is custom clss which is design to pass response in proper format.
		logger.info("Starting to process signup page data");
		System.out.println("Signup");
		AjaxResponseBody resp = new AjaxResponseBody();//create Object of class AjaxResponseBody
		try {//if data is present in emp object then proceed else return false
			if(emp.getEmpId()!=null && emp.getPassword()!=null && emp.getEmail()!=null && emp.getEmpId()!="" && emp.getPassword()!="" && emp.getEmail()!="") {
				if(empService.signup(emp)) {//call service layer method
					resp.setSuccess();//set success code and success message
					resp.setData("Successfully registered as Employee");//set some data
				}
				else {
					resp.setFailure();//set failure code and failure message
					resp.setData("Unable to register.");//set some data
				}
				return resp;//return object of AjaxResponseBody
			}else {
				resp.setFailure();
				resp.setData("Unable to register.");
				return resp;
			}
		}catch(Exception ex) {
			resp.setFailure();
			resp.setData("Unable to register.");
			return resp;
		}
	}

	@PostMapping("/secure/apply-leave")//hitting url /client/apply-leave POST
	public @ResponseBody AjaxResponseBody approveLeave(@RequestBody Employee emp) {
		System.out.println("Apply Leave");
		logger.info("Starting to apply Leave method at controller.");
		AjaxResponseBody resp = new AjaxResponseBody();//crate Obj
		try {
			if(empService.applyLeave(emp)) {//call service layer method
				resp.setSuccess();//succes code and msg
				resp.setData("Successfully applied for leave. Waiting for action.");//some data to some on return
			}
			else {
				resp.setFailure();//failure code and msg
				resp.setData("Sorry! Unable to apply leave.");//some data to show
			}
			return resp;//return obj
		}catch(Exception ex) {//catch all type of exception.
			resp.setFailure();
			resp.setData("Sorry! Unable to apply leave.");
			return resp;//return failure
		}
	}
	
	@PutMapping("/secure/approveLeave")// /client/secure/approveLeave can access only after login
	public @ResponseBody AjaxResponseBody approveLeave(@RequestBody MailBean mail) {
		logger.info("Starting to approve Leave method at controller.");
		System.out.println("Approve Leave");
		AjaxResponseBody resp = new AjaxResponseBody();//create oBj
		try {
			if(empService.approveLeave(mail)) {//call service layer method
				resp.setSuccess();
				resp.setData("Successfully aproved the leave.");
			}
			else {
				resp.setFailure();
				resp.setData("Failure while approving leave.");
			}
			return resp;
		}catch(Exception ex) {
			resp.setData("Failure while approving leave.");
			resp.setFailure();
			return resp;
		}
	}
	
	@PutMapping("/secure/rejectLeave")// cam access only after login /client/secure/rejectLeave
	public @ResponseBody AjaxResponseBody rejectLeave(@RequestBody MailBean mail) {
		logger.info("Starting to reject Leave method at controller.");
		System.out.println("Reject Leave");
		AjaxResponseBody resp = new AjaxResponseBody();
		try {
			if(empService.rejectLeave(mail)) {//call service layer method
				resp.setData("Successfully reject the leave.");
				resp.setSuccess();
			}
			else {
				resp.setData("Failure while rejecting leave.");
				resp.setFailure();
			}
			return resp;
		}catch(Exception ex) {
			resp.setFailure();
			resp.setData("Failure while rejecting leave.");
			return resp;
		}
		
	}
	
	@RequestMapping(value="/secure/applyNewLeave", method=RequestMethod.POST)//to apply new leave once old leave got approved. /client/secure/applyNewLeave
	public @ResponseBody AjaxResponseBody resetOldFlag(@RequestBody Employee emp) {
		logger.info("resetOldFlag : Started to reset old flag");
		//THis method will just set appliedForLeave flag to false so that in UI Apply leave option can be visible
		System.out.println("resetOldFlag");
		AjaxResponseBody resp = new AjaxResponseBody();
		try {
			if(empService.updateLeave(Long.parseLong(emp.getEmpId()))) {//calling service layer method
				resp.setSuccess();
				resp.setData("Reset flag Successfully.");
			}
			else {
				resp.setFailure();
				resp.setData("Unable to reset flag.");
			}
			return resp;
		}catch(Exception ex) {
			resp.setFailure();
			resp.setData("Unable to reset flag.");
			return resp;
		}
	}
	
}
