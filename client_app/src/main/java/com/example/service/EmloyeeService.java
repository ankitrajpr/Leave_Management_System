package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.bean.Employee;
import com.example.bean.MailBean;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service//to define it as service class
@Repository//to define it as repository as well 
public class EmloyeeService implements IEmployeeService {
	//to store sucess code like 200, 205, 202 etc
	public static List<Integer> successCode = new ArrayList<>();
	//to store server error code like 500,501,502 etc
	public static List<Integer> failureCode = new ArrayList<>();
	//for logging our data for future use
	private final Logger logger = LoggerFactory.getLogger(EmloyeeService.class);
	
	public EmloyeeService() {//when ever object of this class create it will add these code to above list .as it is constructor
		EmloyeeService.successCode.add(200);
		EmloyeeService.successCode.add(201);
		EmloyeeService.successCode.add(202);
		EmloyeeService.successCode.add(203);
		EmloyeeService.successCode.add(204);
		
		EmloyeeService.failureCode.add(500);
		EmloyeeService.failureCode.add(501);
		EmloyeeService.failureCode.add(502);
		EmloyeeService.failureCode.add(503);
		EmloyeeService.failureCode.add(504);
	}
	
	@Override
	public boolean signup(Employee emp){//signup Service method
		// encrypt the passwords and Then save.
		logger.info("Signup method has started for "+emp.getEmpId());
		BCryptPasswordEncoder byteCrypt = new BCryptPasswordEncoder();//to encode passord entered by user while register
		emp.setPassword(byteCrypt.encode(emp.getPassword()));//encoding passowrd
		emp.setRole("ROLE_USER");//spring security need some role to access webpage defaul we are adding ROLE_USER
		emp.setEnable(true);//for active user set it true
		logger.info("signup : Password encrypted and setted role and enable for "+emp.getEmpId());
		emp.setLeaveAppliedDate(System.currentTimeMillis());//not required at this location need while apply leave.. but nothing to worry
		String apiUrl = "http://localhost:8412/employee/add";//this is the hitting url to save a new Employee POST
		logger.info("signup : Calling Server '/add' API for "+emp.getEmpId());
		String returnData = callPostMethod(apiUrl,emp);//Calling below POST method to save or data in couchbase
		logger.info("Signup response from server for "+emp.getEmpId()+returnData);
		if("created".equalsIgnoreCase(returnData))//if return created mean success
			return true;
		else
			return false;
	}

	@Override
	public Employee getEmployeeData(long empId) {//getEmployee service layer method
		logger.info("getEmployeeData : GetEmployeeData method started for "+empId);
		String url = "http://localhost:8412/employee/" + empId;//this is the url which will hit in jersey API
		logger.info("getEmployeeData : Calling Server '/getEmployeeByEmpId' API for "+empId);
		String jsonString = callGetMethod(url);//This will call a GET method to hit jersey API which is in leave project
		logger.info("getEmployeeData : GetEmployee response from server for "+empId+". Can not log as have private info.");
		String modifiedJsonStr = jsonString.substring(1, jsonString.length() - 1);//tring to convert into Employee Object as return data is in String format
		Employee emp = new Employee();
		emp = getFormattedEmployee(modifiedJsonStr);//calling methd to convert String to employee
		logger.info("getEmployeeData : GetEmployeeByEmpId success for empId"+emp.getEmpId());
		return emp;
	}
	
	@Override
	public List<Employee> getJuniorEmployee(long empId) {//GetJuniorEmploye Service layer Methd
		logger.info("getJuniorEmployee : Method Started for "+empId);
		String apiUrl = "http://localhost:8412/employee/bossId/"+empId;//This is GET API of Jersey (leave) project hitting url
		List<Employee> employeeList = new ArrayList<>();//obj to store all list of employee return by API
		logger.info("getJuniorEmployee : Calling server GET API '/bossId/{bossId} started for "+empId);
		String jsonString = callGetMethod(apiUrl);//Calling GET method to hit Jersey API 
		logger.info("getJuniorEmployee : Response from server for "+empId+". "+jsonString);
		
		if(jsonString.contains("empId")) {//checking if API has returned some data or not
			String modifiedJsonStr = jsonString.substring(2, jsonString.length() - 2);//trimming so that we can convert it into List of Employee Object
			Employee emp = new Employee();
			//need to generate List of Employee
			if(modifiedJsonStr.contains("},{")) {//if String has this means it has multiple Employee Object in String format
				String empList[] = modifiedJsonStr.split(Pattern.quote("},{"));//split into array
				for(String empString : empList) {//for each part of array which is String of Employee Object 
					emp = getFormattedEmployee(empString);//call this method to format into employee and store in emp object
					employeeList.add(emp);//add emp object in List of Employee Object
				}
			}else {
				emp = getFormattedEmployee(modifiedJsonStr);//if API return has only one Employee Object then directly call this to format into employee object
				employeeList.add(emp);//adding in lIst of employee Object
			}
		}
		logger.info("getJuniorEmployee : Successfully return employee list for "+empId+". "+jsonString);
		return employeeList;//return All junior Level employee who are under him
	}
	
	@Override
	public String callGetMethod(String apiUrl) {//This is the common method which has been use to hit a GET API of Jersey
		logger.info("callGetMethod : Invoking GET API method for url: apiUrl");
		String jsonString = "";//this is to store return data from api
		try {
			URL url = new URL(apiUrl);//convert String to url
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//open connection with above url
			conn.setRequestMethod("GET");//with GET type
			conn.setRequestProperty("Accept", "application/json");//whci can accept Json Type data
			if (EmloyeeService.successCode.contains(conn.getResponseCode())) {//checking if connection got success or failure . 200 mean success 500 means failure
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));//if success then store the returned data by creating String buffer as data may come in many lines.
				String output;
				logger.info("callGetMethod : Data returned from server.");
				while ((output = br.readLine()) != null) {//while data is coming do below
					jsonString += output;//store it in jsonString which we will use later
				}
				conn.disconnect();//we have store returned data so close connection.
				logger.info("callGetMethod : Data returned from server. "+jsonString);
			}
		} catch (MalformedURLException e) {//incase if error occured while converting string to URL
			logger.error("callGetMethod : Error while geting data from server:"+e.getMessage());
		} catch (IOException e) {//error occure while writing data in buffered String
			logger.error("callGetMethod : Error while geting data from server:"+e.getMessage());
		}
		return jsonString;//returned the data got from API
	}
	
	@Override
	public String callPostMethod(String apiUrl, Employee emp) {//This is the common method which has been use to call a POST API as this need to pass body along with header
		logger.info("callPostMethod : Invoking POST API method for url: apiUrl");
		ObjectMapper mapper = new ObjectMapper();//to convert Employee object into JSON format as API never accept Employee object
		try {
			URL url = new URL(apiUrl);//convert String to URL
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//open connection with above url
			conn.setDoOutput(true);//set true to return output
			conn.setRequestMethod("POST");//connection type is POST
			conn.setRequestProperty("Content-Type", "application/json");//send data is JSON format
			String jsonString = mapper.writeValueAsString(emp);//converting employee object to JSON string with mapper
			OutputStream os = conn.getOutputStream();//Create obj of output write to write/send data over connection
			os.write(jsonString.getBytes());//write this String over connected connection.. Means send the body in json fomat in POST Request
			os.flush();//data sent or written so flush it
			if (EmloyeeService.successCode.contains(conn.getResponseCode())) {//now check if connection got success or not
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));//if connection got success then get the returned data by Buffered String input object
				String output;
				System.out.println("Output from Server .... \n");
				logger.info("callPostMethod : Data returned from server. "+emp.getEmpId());
				while ((output = br.readLine()) != null) {//write to jsonString until API is returning data 
					System.out.println(output);//Since We know out POST API will not send much data other than true or success so we r not storing it.
				}
				conn.disconnect();//disconnect the connection. //if Connection is success means it got success else it will throw exception.
			}else {
				//throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				return "Server Internal Error";//if connection got failed mean no success
			}
		} catch (MalformedURLException e) {//error while converting String to URL
			logger.error("callPostMethod : Error while geting data for empId "+emp.getEmpId()+" from server:"+e.getMessage());
		} catch (IOException e) {//Input output exception
			logger.error("callPostMethod : Error while geting data for empId "+emp.getEmpId()+" from server:"+e.getMessage());
		}
		return "created";
	}
	
	// Common method To fetch Data in correct Object
	public Employee getFormattedEmployee(String s) {//This is the method which has been use to format the String into Employee object
		logger.error("getFormattedEmployee : Started to format data.");
		Employee employee = new Employee();//create empty employee object
		String emp[] = s.split(",");//split the String on basis of ","
		for (String str : emp) {//for each part of created array do below
			String attrbutes[] = str.split(":");//since Staing is seperated by ":" so split it on basis of ":"
			String tempX = attrbutes[0].replace("\"", "");//store first half in tempX variable
			
			if ("empId".equalsIgnoreCase(tempX)) {//check if tempX is any attribute of Employee. if yes store the second half in empty employee Object
				employee.setEmpId((attrbutes[1].replace("\"", "")));
			}
			if ("empName".equalsIgnoreCase(tempX)) {
				employee.setEmpName((attrbutes[1].replace("\"", "")));//store which was afer colon.
			}
			if ("email".equalsIgnoreCase(tempX)) {
				employee.setEmail((attrbutes[1].replace("\"", "")));//store which was afer colon.
			}
			if ("jobLevel".equalsIgnoreCase(tempX)) {
				employee.setJobLevel(Integer.parseInt(attrbutes[1].replace("\"", "")));//store which was afer colon.
			}
			if ("leaveAllocation".equalsIgnoreCase(tempX)) {
				if ("true".equals((attrbutes[1].replace("\"", ""))))
					employee.setLeaveAllocation(true);
				else
					employee.setLeaveAllocation(false);
			}
			if ("appliedForLeave".equalsIgnoreCase(tempX)) {
				if ("true".equals((attrbutes[1].replace("\"", ""))))
					employee.setAppliedForLeave(true);
				else
					employee.setAppliedForLeave(false);
			}
			if ("leaveDays".equalsIgnoreCase(tempX)) {
				employee.setLeaveDays(Integer.parseInt(attrbutes[1].replace("\"", "")));//store which was after colon. after removing extra double quote
			}
			if ("bossId".equalsIgnoreCase(tempX)) {
				employee.setBossId(Long.parseLong(attrbutes[1].replace("\"", "")));//store which was after colon. after removing extra double quote
			}
			if ("totalLeave".equalsIgnoreCase(tempX)) {
				employee.setTotalLeave(Integer.parseInt(attrbutes[1].replace("\"", "")));//store which was after colon. after removing extra double quote
			}
			if ("leaveAppliedDate".equalsIgnoreCase(tempX)) {
				employee.setLeaveAppliedDate(Long.parseLong(attrbutes[1].replace("\"", "")));//store which was after colon. after removing extra double quote
			}
		}
		
		return employee;//returned the formatted Employee Object
	}

	@Override
	public boolean applyLeave(Employee emp) {//this is service layer ApplyLeave Method use to apply leave
		logger.info("applyLeave: Started."+emp.getEmail());
		if(emp.getEmpId() != null && emp.getEmpId() != "" && emp.getLeaveDays() != 0 && emp.getTotalLeave()!=0) {//check if necessary data is present in employee Object
			logger.info("applyLeave: Condition pass.");
			//check if total leave is greater than applied leaves
			if(emp.getTotalLeave()-emp.getLeaveDays()>=0) {//check if employee has not leave shortage
				logger.info("applyLeave: Valid leave request."+emp.getEmail());
				Calendar cal = Calendar.getInstance();//to get current time in millisecond
				long timeInMili = cal.getTimeInMillis();//to get current time in millisecond
				emp.setLeaveAppliedDate(timeInMili);//set the time in employee object
				//Need to call Post Api
				String url = "http://localhost:8412/employee/applyLeave";//this is the URL to jersey API to apply Leave
				logger.info("applyLeave: Calling PUT API url : "+url);
				String result = callPutMethod(url, emp);//calling the PUT Jersey API with above url/string
				if("created".equalsIgnoreCase(result))//created means success as we know our API will not return much than true or success as it is PUT API
					return true;
			}
		}
		logger.info("applyLeave: Condition fail."+emp.getEmail());
		return false;
	}

	@Override
	public boolean approveLeave(MailBean mailObj) {//This is approve leave Service layer method
		logger.info("approveLeave: Started.");//We have MailObject as we need to send mail to 3 person so we have store email of 2 person in mailObj and third we can call API to get
		Employee emp = new Employee();//creating empty employee Object
		if(mailObj.getApplicantId()!=null && mailObj.getApplicantId()!=0 && mailObj.getTotalLeave()!=0 && mailObj.getLeaveDays()!=0) {//checking the required attribute is present inobject or not
			logger.info("approveLeave: Condition pass."+emp.getEmpId());
			emp.setEmpId(mailObj.getApplicantId().toString());//setting employeeId in employee Obj
			emp.setTotalLeave(mailObj.getTotalLeave());//setting total leave to employee Object from mailObj
			emp.setLeaveDays(mailObj.getLeaveDays());//setting leave days in employee Object from MailObject
			String url = "http://localhost:8412/employee/approveLeave";//This is the API URL to hit Jersey API to apply Leave ad update data in couchbase
			logger.info("approveLeave: Calling PUT API url : "+url);
			String result = callPutMethod(url, emp);//calling PUT Jersey API with this method below
			if("created".equalsIgnoreCase(result)) {//As usual PUT method will return true or success so We have manipulated it to return created instead of success else Server Error
				//Need to send mail for approval
				/*
				Employee bossEmp = null;
				if(mailObj.getBossId() != mailObj.getActionTakerId()) {
					String data = callGetMethod("http://localhost:8412/employee/"+mailObj.getBossId());
					bossEmp = getFormattedEmployee(data);
				}
				logger.info("approveLeave: Sending mail for approval for empId"+emp.getEmpId());
				System.out.println("SimpleEmail Start");
				final String fromEmail = "abc@gmail.com"; //requires valid gmail id
				final String password = "password"; // correct password for gmail id
				
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
				props.put("mail.smtp.port", "587"); //TLS Port
				props.put("mail.smtp.auth", "true"); //enable authentication
				props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
				//props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
				//props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl.trust", "*");
                //create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, password);
					}
				};
				Session session = Session.getInstance(props, auth);
				String messageBody = "Hi "+mailObj.getApplicantId()+", \n"
						+"Your request for leave has been approved by "+mailObj.getActionTakerId()
						+"Thanks \n"+mailObj.getActionTakerEmailId();
				if(bossEmp != null) {
					MailService.sendEmail(session, bossEmp.getEmail(),"Leave Approval Mail",messageBody);
					logger.info("approveLeave: mail successfully sent for empId "+bossEmp.getEmpId());
				}
				MailService.sendEmail(session, mailObj.getApplicantEmailId(),"Leave Approval Mail",messageBody);
				logger.info("approveLeave: mail successfully sent for empId "+mailObj.getApplicantId());
				MailService.sendEmail(session, mailObj.getActionTakerEmailId(),"Leave Approval Mail",messageBody);
				logger.info("approveLeave: mail successfully sent for empId "+mailObj.getActionTakerId());*/
			    return true;
			}
			else 
				return false;
		}
		logger.info("approveLeave: Condition fail.");
		return false;
	}
	
	@Override
	public boolean rejectLeave(MailBean mailObj) {//This is excat similar to approve Leave here only we are hitting /rejectLeave Jersey API
		logger.info("rejectLeave: Started.");
		Employee emp = new Employee();
		if(mailObj.getApplicantId()!=null && mailObj.getApplicantId()!=0) {
			logger.info("rejectLeave: Condition pass.");
			emp.setEmpId(mailObj.getApplicantId().toString());
			String url = "http://localhost:8412/employee/rejectLeave";//Jersey Hitting URL
			logger.info("rejectLeave: Calling PUT API url :"+url);
			String result = callPutMethod(url, emp);//PUT method to call
			if("created".equalsIgnoreCase(result)) {//returned data from API
				logger.info("rejectLeave: Sending mail for rejection for empId "+emp.getEmpId());
				//Need to send mail	for rejection
				/*
				Employee bossEmp = null;
				if(mailObj.getBossId() != mailObj.getActionTakerId()) {
					String data = callGetMethod("http://localhost:8412/employee/"+mailObj.getBossId());
					bossEmp = getFormattedEmployee(data);
				}
				final String fromEmail = "abc@gmail.com"; //requires valid gmail id
				final String password = "123"; // correct password for gmail id
				
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
				props.put("mail.smtp.port", "587"); //TLS Port
				props.put("mail.smtp.auth", "true"); //enable authentication
				props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
				//props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
				//props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl.trust", "*");
                //create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, password);
					}
				};
				Session session = Session.getInstance(props, auth);
				
				String messageBody = "Hi "+mailObj.getApplicantId()+", \n"
						+"Your request for leave has been approved by "+mailObj.getActionTakerId()
						+"Thanks \n"+mailObj.getActionTakerEmailId();
				if(bossEmp != null) {
					MailService.sendEmail(session, bossEmp.getEmail(),"Leave Approval Mail",messageBody);
					logger.info("approveLeave: mail successfully sent for empId "+bossEmp.getEmpId());
				}
				MailService.sendEmail(session, mailObj.getApplicantEmailId(),"Leave Approval Mail",messageBody);
				logger.info("approveLeave: mail successfully sent for empId "+mailObj.getApplicantId());
				MailService.sendEmail(session, mailObj.getActionTakerEmailId(),"Leave Approval Mail",messageBody);
				logger.info("approveLeave: mail successfully sent for empId "+mailObj.getActionTakerId());
				logger.info("rejectLeave: mail successfully sent for empId "+emp.getEmpId());*/
				return true;
			}else
				return false;
		}
		logger.info("rejectLeave: Condition fail.");
		return false;
	}

	@Override
	public String callPutMethod(String apiUrl,Employee emp) {//This is the common PUT method which has been call to hit Jersey PUT API
		logger.info("callPUTMethod : Invoking PUT API method for url: apiUrl");
		ObjectMapper mapper = new ObjectMapper();//Create obj of Mapper to map employee into JSON string as like POST we need to send data in json format.
		try {
			URL url = new URL(apiUrl);//convert To URL from string
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//open connection with above url
			conn.setDoOutput(true);//set true to return 
			conn.setRequestMethod("PUT");//connection type PUT
			conn.setRequestProperty("Content-Type", "application/json");//send data in JSON
			if(emp!=null) {//if emp object is noul mean we have nothing to send else need to parse employee object nad send over connection
				String jsonString = mapper.writeValueAsString(emp);//parsing / converting emp object to string
				OutputStream os = conn.getOutputStream();//create object to write over network
				os.write(jsonString.getBytes());//start writing on network or send over network
				os.flush();//data sent so flush the os object
			}
			if (EmloyeeService.successCode.contains(conn.getResponseCode())) {//check if the connection was success or not
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));//if connection was success means 200 or 201 or 202
				String output;
				System.out.println("Output from Server .... \n");
				logger.info("callPUTMethod : Data returned from server");
				while ((output = br.readLine()) != null) {//store data while its returning. As We know it will return true so no need to save anywhere
					System.out.println(output);
					logger.info(output);
				}
				conn.disconnect();//got the data so disconnect the network
			}else {
				//throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				return "Server Internal Error";//it connection return 500 501 502 etc then return this result
			}
		} catch (MalformedURLException e) {
			logger.error("callPUTMethod : Error while geting data from server:"+e.getMessage());
		} catch (IOException e) {
			logger.error("callPUTMethod : Error while geting data from server:"+e.getMessage());
		}
		return "created";//means operation got success
	}
	
	@Override
	public boolean updateLeave(long empId) {//This is the method to update few flag when employee will apply new Leave.
		String url="http://localhost:8412/employee/update/"+empId;//this is Jersey PUT API
		String result = callPutMethod(url,null);//calling Jersey PUT API
		if("created".equalsIgnoreCase(result)) {//if API return created means true
			return true;
		}else {
			return false;
		}
	}
	
}
