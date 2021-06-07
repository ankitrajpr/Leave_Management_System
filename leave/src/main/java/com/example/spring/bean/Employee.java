package com.example.spring.bean;


import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import com.couchbase.client.java.repository.annotation.Id;

@Document
public class Employee {

	@Id 
	@GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES,delimiter = "_")//This is the line which will determine how our document Id will be generate while creating new Document 
	private String docId;// document id
	@IdPrefix(order=0)
    private String userPrefix ="Employee";//This will get added while generate document id
	@IdAttribute
	private long empId; //employee id this will also get append in document id Sample docId is Employee_12345
	private String empName;//name
	private String email;	//Email Id
	private String password;//password
	private String role;//role need for Spring security login they have role features so..
	private boolean enable; //Just to check if employee is active .. by default is active
	private int totalLeave;//also will host remaining leaves
	private long bossId; //help full in searching 2 down level employee
	private int jobLevel; //Employee job level also help in searching down level 
	private int leaveDays; //no. of days employee apply fo rleave
	private boolean leaveAllocation; // approved or not.
	private long leaveAppliedDate;	//Date on which date employee applyied for leave
	private boolean appliedForLeave;	//true if he applied and false if not	//use for querying
	//Constructor 
	public Employee() {
	}
	//Getter Setter
	
	public long getEmpId() {
		return empId;
	}
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public int getTotalLeave() {
		return totalLeave;
	}
	public void setTotalLeave(int totalLeave) {
		this.totalLeave = totalLeave;
	}
	public long getBossId() {
		return bossId;
	}
	public void setBossId(long bossId) {
		this.bossId = bossId;
	}
	public int getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(int jobLevel) {
		this.jobLevel = jobLevel;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}
	public boolean isLeaveAllocation() {
		return leaveAllocation;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLeaveAllocation(boolean leaveAllocation) {
		this.leaveAllocation = leaveAllocation;
	}

	public long getLeaveAppliedDate() {
		return leaveAppliedDate;
	}

	public void setLeaveAppliedDate(long leaveAppliedDate) {
		this.leaveAppliedDate = leaveAppliedDate;
	}

	public boolean isAppliedForLeave() {
		return appliedForLeave;
	}

	public void setAppliedForLeave(boolean appliedForLeave) {
		this.appliedForLeave = appliedForLeave;
	}
	
	
}
