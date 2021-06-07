<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Signup Page</title>
<style type="text/css">
.requiredastrick{
color:red;
}

.labelClass{
color: blueviolet;
width: 45%;
border: 1px soild red !important;
float: left;
text-align: right;
font-size: larger;
margin-right: 2px;
}


.inputClass{
color: blueviolet;
width: 45%;
border: 1px soild red !important;
display: -webkit-box;
}
</style>
<script src="/js/jquery-3.2.0.min.js"></script>
<script src="/js/welcome.js"></script>
<%-- <meta name="_csrf" content="${_csrf.token}"> --%>
<%-- <meta name="_csrf_header" content="${_csrf.headerName}"> --%>
</head>
<body>
	<div style="width: 100%; margin-top:5%;"><div style="width:50%; margin:0 auto;">
	<h1 style="text-align:center;"> Leave System Signup Page</h1>
<form>
<div class="labelClass">
<label>Employee Id<span class="requiredastrick">*</span> :</label></br>
<label>Employee Name<span class="requiredastrick">*</span> :</label></br>
<label>Email<span class="requiredastrick">*</span> :</label></br>
<label>Password<span class="requiredastrick">*</span> :</label></br>
<label>Boss Employee Id<span class="requiredastrick">*</span> :</label></br>
<label>Job Role<span class="requiredastrick">*</span> :</label></br>
<label>Total Leave<span class="requiredastrick">*</span> :</label></br>
</div>
<div class="inputClass">
	<input type="text" name="empId" placeholder="Enter Your Employee Id" /><br>
	<input type="text" name="empName" placeholder="Enter Your Name" /> <br>
	<input type="email" name="email" placeholder="Enter Your Email" /> <br>
	<input type="password" name="password" placeholder="Enter Your Password" /> <br>
	<input type="text" name="bossId" placeholder="Enter Your Boss Employee Id" /><br>
	<select id="jobLevel" name="jobLevel">
			<option value="5" selected="selected">Employee</option>
			<option value="4">Team Lead</option>
			<option value="3">Manager</option>
			<option value="2">Senior Manager</option>
			<option value="1">CEO</option>
	</select></br>
	<input type="text" name="totalLeave" placeholder="Enter Your Total Leaves Left" /> </br>
	<input type="hidden" name="_csrfToken" value="${_csrf.token}" />
</div><br>
<div style="text-align:center">
<input type="button" value="Register" onclick="javascript:signup();">
<input type="reset" value="Reset">
</div><br>
<div style="text-align:center">
<a href="/client/login">Login</a>
</div>
</form>
</div></div>
</body>
</html>