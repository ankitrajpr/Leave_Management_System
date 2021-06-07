<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>Welcome</title>
<link href="/css/welcome.css" rel="stylesheet">
<script src="/js/jquery-3.2.0.min.js"></script>
<script src="/js/welcome.js"></script>
<meta name="_csrf" content="${_csrf.token}">
</head>
<body>
	<div style="width: 100%; margin-top:5%;">
		<div style="width:80%; margin:0 auto;border:2px solid violet;">
			<h1 style="text-align:center;">Leave System Welcome Page </h1>
			<div style="text-align:right;">
				<div style="font-size:22px;">
					Hi ${employee.empName} , 
					<br>
					<h5 id="loggedInEmailId" style="display:inline;"> ${employee.email}</h5>
					#<h5 id="actionTakerId" style="display:inline;">${employee.empId}</h5>
				</div>
				<div>
					<c:url value="/client-logout" var="logoutUrl" />
					<form method="POST" action="${logoutUrl}" id="logoutForm">
						<input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}">
						<input type="submit" value="Logout"><a href="/client/help">Help</a>
					</form>
				</div>
			</div>
			<div style="text-align:center;">
				<c:if test="${employee.appliedForLeave}">
					Total Leaves : ${employee.totalLeave} <br>
					Leave Status : 
					<c:if test="${employee.leaveAllocation}">
						<p style="color:green;display:inline">Approved</p>
							<input type="button" value="Apply New Leave" onclick="javascript:applyNewLeave()"><br>
					</c:if>
					<c:if test="${not employee.leaveAllocation}">
						<p style="color:orange;display:inline;">Pending</p>
					</c:if> 				
				</c:if>
				
				<c:if test="${not employee.appliedForLeave}">
					Total Leaves : <p style="display:inline;" id="totalLeave">${employee.totalLeave}</p><br>
					Select Leave Days:
					<select name="leaveDays">
						<option>--Select--</option>
						<c:forEach var ="i" begin="1" end ="${employee.totalLeave}">
							<option value="${i}">${i}</option>
						</c:forEach>
					</select>	<br>
					<input type="button" value="Apply Leave" onclick="javascript:applyeLeave();"><br>
				</c:if>
			</div>
			<div style="padding-top:2%; text-align:center;">
				<input type="button" value="Show Details" id="show" onclick="javascript:showDetail();">
				<input type="button" value="Hide Details" id="hide" style="display:none;" onclick="javascript:hideDetail();">
			</div>
			<div id="below">
				<div class="text-center">
				<c:if test="${not empty employeeList}">
				<jsp:useBean id="dateValue" class="java.util.Date"/>
					<table id="LeaveTable">
						<thead>
				            <tr>
				                <th>Employee Id</th>
								<th>Employee Name</th>
								<th>Email Id</th>
								<th>Job Level</th>
								<th>Total Leaves</th>
								<th>Senior Employee Id</th>
								<th>Leave Days</th>
								<th>Leave Applied Date</th>
								<th>Leave Status</th>
								<th>Action</th>
				            </tr>
				        </thead>
				        <tbody>
							<c:forEach items="${employeeList}" var="employee" varStatus="status">
								<tr class="row100 body">
									<td class="column1" style="width:2%;" id="empId_${status.index}">${employee.empId}</td>
									<td class="column2" style="width:20%;">${employee.empName}</td>
									<td class="column3" style="width:20%;" id="empEmailId_${status.index}">${employee.email}</td>
									<td class="column4" style="width:2%;">${employee.jobLevel}</td>
									<td class="column5" style="width:2%;" id="totalLeave_${status.index}">${employee.totalLeave}</td>
									<td class="column6" style="width:2%;" id="bossId_${status.index}">${employee.bossId}</td>
									<td class="column7" style="width:2%;" id="leaveDays_${status.index}">${employee.leaveDays}</td>
										<jsp:setProperty name="dateValue" property="time" value="${employee.leaveAppliedDate}"/>
									<td class="column8" style="width:2%;" >
										<fmt:formatDate value="${dateValue}" pattern="MM/dd/yyyy"/>
									</td>
									<td class="column9" style="width:3%;color:green;">
										<c:if test="${employee.leaveAllocation}">Approved</c:if>
									</td>
									<td class="column10" style="width:13%;">
										<c:if test="${not employee.leaveAllocation}">
											<input type="button" value="Approve" style="background:green;" onclick="javascript:takeAction('approve', ${status.index} ,this);">
											<input type="button" value="Reject" style="background:red;" onclick="javascript:takeAction('reject', ${status.index} ,this);">
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
	       			 </table>
	       			 </c:if>
				</div>
			</div>
		</div>
	</div>
</body>
</html>