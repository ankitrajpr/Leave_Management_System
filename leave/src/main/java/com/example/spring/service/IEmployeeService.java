package com.example.spring.service;

import java.util.List;
import java.util.Optional;

import com.couchbase.client.java.document.json.JsonObject;
import com.example.spring.bean.Employee;

public interface IEmployeeService {
	
	public Optional<Employee> getEmployeeByDocumentId(String docId);
	
	public Employee getEmployeeByEmpId(long empId);
	
	public boolean validateEmployeeByEmpId(long empId);
	
	public Employee addNewEmployee(Employee emp);	
	
	public boolean updateEmployeeByEmpId(long empId);
	
	public boolean deleteEmployeeByEmpId(long empId);
	
	public List<Employee> getJuniorEmployee(long bossId);
	
	public boolean applyLeave(Employee emp);
	
	public boolean approveLeave(Employee emp);

	public boolean rejectLeave(Employee emp);
	
	public Employee customObjectParser(JsonObject leaveObj);
	
}
