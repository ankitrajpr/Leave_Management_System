<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>Documentation</title>
<link href="/css/welcome.css" rel="stylesheet">
<script src="/js/jquery-3.2.0.min.js"></script>
</head>
<body>
<!-- There is nothing to explain -->
	<div style="width: 100%; margin-top:5%;">
		<div style="width:80%; margin:0 auto;border:2px solid violet;">
			<p>Here you can add all other details of application.</p>
			<h2>API Controller</h2>
			<dl>
				<dt>Add New Employee</dt>
					<dd>This api is use to add new employee or for signup purpose. <br>
						Hitting URL : http://localhost:8412/employee/add <br>
						Method : POST <br>
						Data : Employee Object <br>
						Resonse : Success<br>
					</dd>
				<dt>Get Employee</dt>
					<dd>This api is use to fetch any employee with help of employee Id. <br>
						Hitting URL : http://localhost:8412/employee/{empId} <br>
						Method : GET <br>
						Data : {} <br>
						Response : Json string of Employee <br>
					</dd>
				<dt>Get All Employee</dt>
					<dd>This api is use to fetch list of employee who are under the loggedin user till 2 level below.<br>
						Hitting URL : http://localhost:8412/employee/bossId/{bossId} <br>
						Method : GET <br>
						Data : {} <br>
						Response : Json string of list of Employee <br>
					</dd>
				<dt>Validate Employee</dt>
					<dd>This api is use to check wether employe exist or not.<br>
						Hitting URL : http://localhost:8412/employee/validate/{empId} <br>
						Method : GET <br>
						Data : {} <br>
						Response : true / Success <br>
					</dd>
				<dt>Update Employee</dt>
					<dd>This api is use to update few of the attribute/column of Employee <br>
						Hitting URL : http://localhost:8412/employee/update/{empId} <br>
						Method : PUT <br>
						Data : {} <br>
						Resonse : Success<br>
					</dd>
				<dt>Apply Leave</dt>
					<dd>This api is use to apply leave. <br>
						Hitting URL : http://localhost:8412/employee/applyLeave<br>
						Method : PUT <br>
						Data : Employee Object <br>
						Resonse : Success<br>
					</dd>
				<dt>Approve Leave</dt>
					<dd>This api is use to add new employee or for signup purpose. <br>
						Hitting URL : http://localhost:8412/employee/approveLeave <br>
						Method : PUT <br>
						Data : Employee Object <br>
						Resonse : Success <br>
					</dd>
				<dt>Reject Leave</dt>
					<dd>This api is use to add new employee or for signup purpose. <br>
						Hitting URL : http://localhost:8412/employee/rejectLeave <br>
						Method : PUT <br>
						Data : Employee Object <br>
					</dd>
				<dt>Delete Employee</dt>
					<dd>This api is use to add new employee or for signup purpose. <br>
						Hitting URL : http://localhost:8412/employee/delete/{empId} <br>
						Method : POST <br>
						Data : {} <br>
						Response : Success<br>
					</dd>
				<dt>Delete Document</dt>
					<dd>This api is use to delete the created document id only by documentId. <br>
						Hitting URL : http://localhost:8412/employee/document/{docId}<br>
						Method : POST <br>
						Data : {} <br>
						Resonse : Success <br>
					</dd>
				<dt>Annually Add default leave days</dt>
					<dd>This is a cron job which will only run once at 00:00 1st January of every Year <br> to update the total leave by adding remaining and the newly allocated leaves to each employee.<br>
			</dl>
			
			<br>
			<h2> Client Controller</h2>
			<dl>
				<dt>Signup</dt>
					<dd>This is use to signup by any new employe.<br>
					All the feilds are mandatary to fill.</dd>
				<dt>Login</dt>
					<dd>This is use to login by registered employe.<br>
					Spring security has been use for login, which use CSRF(Cross Site Request Forgery) methodology. 
					</dd>
				<dt>Welcome</dt>
					<dd>This is use to the page where user can apply for leave.<br>
					User can approve / reject the leave status<br>
					</dd>
				<dt>Error</dt>
					<dd>This is use to redirect unauthorize user to error page.</dd>
				<dt>Help</dt>
					<dd>This is use to visit help page.</dd>
			</dl>
		</div>
	</div>
</body>
</html>