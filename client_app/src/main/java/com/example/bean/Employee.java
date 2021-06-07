package com.example.bean;

public class Employee {

	private String empId;	//For Employee unique Id
	private String empName;	//name
	private String email;	//Email to send mail
	private String password;	//password
	private String role;	//role need for Spring security login they have role features so..
	private boolean enable; //Just to check if employee is active .. by default is active
	private int totalLeave;	//also will host remaining leaves
	private long bossId; 	//help full in searching 2 down level employee
	private int jobLevel; 	//Employee job level also help in searching down level 
	private int leaveDays; 	//no. of days employee apply for leave
	private boolean leaveAllocation; //for leave approval/reject
	private long leaveAppliedDate;	//this will be first time as system date...on date which employee applied.
	private boolean appliedForLeave;	//true if applied and false if not 
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
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
	public int getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}
	public boolean isLeaveAllocation() {
		return leaveAllocation;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAppliedForLeave() {
		return appliedForLeave;
	}
	public void setAppliedForLeave(boolean appliedForLeave) {
		this.appliedForLeave = appliedForLeave;
	}
	
	
	
}
