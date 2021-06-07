package com.example.service;

import java.util.List;

import com.example.bean.Employee;
import com.example.bean.MailBean;
//This is the Interface where we have declare all method
public interface IEmployeeService {

	public boolean signup(Employee emp);
	public Employee getEmployeeData(long empId);
	public List<Employee> getJuniorEmployee(long empId);
	public String callPostMethod(String url, Employee emp);
	public String callGetMethod(String url);
	public String callPutMethod(String url, Employee emp);
	public Employee getFormattedEmployee(String s);
	public boolean applyLeave(Employee emp);
	public boolean approveLeave(MailBean mailObj);
	public boolean rejectLeave(MailBean mailObj);
	public boolean updateLeave(long empId);
}
