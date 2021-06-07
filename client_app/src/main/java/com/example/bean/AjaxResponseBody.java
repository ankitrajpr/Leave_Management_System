package com.example.bean;


public class AjaxResponseBody {

	private String statusMessage;//store success messages
	private String statusCode;//success code
	private String data;//some data
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setSuccess() {//set Success
		this.statusCode="200";
		this.statusMessage="SUCCESS";
	}
	public void setFailure() {//set Failure
		this.statusCode="403";
		this.statusMessage="ERROR";
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override//just in case we need to return as String of this class.
	public String toString() {
		return "ajaxResp [statusCode="+statusCode+
				",statusMessage="+statusMessage+
				",data="+data+
				"]";
	}
}
