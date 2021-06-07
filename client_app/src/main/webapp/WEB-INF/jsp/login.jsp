<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>
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
</head>
<body>
	<div style="width: 100%; margin-top:5%;">
		<div style="width:50%; margin:0 auto;">
			<h1 style="text-align:center;">Leave System Login Page</h1>
				<a href="/client/help">Help</a>
				<form name='loginForm' id='LoginForm' action="<c:url value='/client-login' />" method='POST'>
				<c:if test="${not empty error}">
				<div class="error">${error}</div>
				</c:if>
				<c:if test="${not empty logout}">
				<div class="success">${logout}</div>
				</c:if>
				<div class="labelClass">
					<label>Employee Id<span class="requiredastrick">*</span> :</label></br>
					<label>Password<span class="requiredastrick">*</span> :</label></br>
					<!-- Below is the reuired tag for each action as it generate csrf token whci get validate by Spring security -->
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</div>
				<div class="inputClass">
					<input type="text" name="emp_id"/><br>
					<input type="password" name="emp_password" /> <br>
				</div>
				<div style="text-align:center;margin-top: 2%;">
					<input type="submit" value="Login">
					<input type="reset" value="Reset">
				</div>
				<br>
				<div style="text-align:center;">
					<a href="/client/signupPage">Signup</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>