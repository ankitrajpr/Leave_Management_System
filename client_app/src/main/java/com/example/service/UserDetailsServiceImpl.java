package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.bean.Employee;



@Repository	//This is the class where object of UserDetailsServiceImpl
@Service	//to define it as service class
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {
		Employee emp = new Employee();	//empty EMployee Obj
		try {
			//This is how we make a call GET call.
			URL url = new URL("http://127.0.0.1:8091/employee/"+empId);	//This is the URL of Jersey to get EMployee data with empId
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();	//Open connection with this url
			conn.setRequestMethod("GET");	//Get Method
			conn.setRequestProperty("Accept", "application/json");	//data type JSON
			if (conn.getResponseCode() != 200) {	//check if connection got success or not
				//do nothing as Employee Details is Emplty
				//Set Employee Password as some random number so that null password do not get success
				emp.setPassword("demo"+Math.random());	//seting radom passord if no user found for this empid.
			}else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));	//tp store return data used BufferedReader
				String output;
				String jsonString ="";
				//Data return from API
				while ((output = br.readLine()) != null) {	//while data is being returned by API store it in jsonString
					jsonString+=output;
				}
				conn.disconnect();//data received . so disconnect the connection
				//To remove unwanted string which may cause problem in spliting
				String modifiedJsonStr = jsonString.substring(1,jsonString.length()-1);//Trim this returned String so that we can format this string into EMployee Objec
				emp = getCorrectEmloyee(modifiedJsonStr);//Calling this method to format String to Employee
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//exception while generating url
		}catch (IOException e) {
			e.printStackTrace();//other I/O exception
		} 
		//To Put Role as Granted Authority as per Spring Security
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(emp.getRole());//Now setting the employee Role IN grantedAuthority to check that this role can access this whch URL which we defined while config
		User user = new User(emp.getEmpId(),emp.getPassword(),Arrays.asList(grantedAuthority));	//Now creating User Objec which is required by Spring Security to match with User input data . "User" Class is by default
		//Below User Details will be match with what we have Provied in ogin page. //Here password returned from API is encrypted 
		//And Spring security will also check with security encrytper
		UserDetails userDetails = (UserDetails)user;//return UserDetails which is alos By default of Spring security
		return userDetails;
	}
	
	//This method to fetch correct data from returned JSON string from API
	public Employee getCorrectEmloyee(String s) {//This is the method which will convert String to Employee Object
		Employee employee = new Employee();
		String emp[] = s.split(",");//Split String based on "'"
		for(String str:emp) {	//for each element in Array
			String attrbutes[] = str.split(":"); //Split by ":"
				String tempX = attrbutes[0].replace("\"", "");	//check if first half is empId
				if("empId".equalsIgnoreCase(tempX)) {
					employee.setEmpId((attrbutes[1].replace("\"", "")));	//store second half in emp Object
				}
				if("password".equalsIgnoreCase(tempX)) {
					employee.setPassword((attrbutes[1].replace("\"", "")));//store second half in emp Object
				}
				if("role".equalsIgnoreCase(tempX)) {
					employee.setRole((attrbutes[1].replace("\"", "")));//store second half in emp Object
				}
				if("enable".equalsIgnoreCase(tempX)) {
					if("true".equals(attrbutes[1].replace("\"", "")))//store second half in emp Object
						employee.setEnable(true);
					else
						employee.setEnable(false);
				}
		}
		return employee;//retur Proper Employee Object
	}

}
