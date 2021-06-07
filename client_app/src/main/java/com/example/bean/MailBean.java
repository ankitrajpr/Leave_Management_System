package com.example.bean;

public class MailBean {

	private Long applicantId;//to store employee Id
	private String applicantEmailId;//to store employee email id
	private int totalLeave;//to store total leave of employee
	private int leaveDays;//store leave days of employee
	private long bossId;//to store the senior employee id
	private long actionTakerId;//to store the empId of that person who has approve /reject leave application
	private String actionTakerEmailId;//to store the email id of that person who has approve /reject leave application
	public Long getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}
	public String getApplicantEmailId() {
		return applicantEmailId;
	}
	public void setApplicantEmailId(String applicantEmailId) {
		this.applicantEmailId = applicantEmailId;
	}
	public long getBossId() {
		return bossId;
	}
	public void setBossId(long bossId) {
		this.bossId = bossId;
	}
	public long getActionTakerId() {
		return actionTakerId;
	}
	public void setActionTakerId(long actionTakerId) {
		this.actionTakerId = actionTakerId;
	}
	public String getActionTakerEmailId() {
		return actionTakerEmailId;
	}
	public void setActionTakerEmailId(String actionTakerEmailId) {
		this.actionTakerEmailId = actionTakerEmailId;
	}
	public int getTotalLeave() {
		return totalLeave;
	}
	public void setTotalLeave(int totalLeave) {
		this.totalLeave = totalLeave;
	}
	public int getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}
	
	
}
