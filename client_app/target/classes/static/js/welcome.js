// To call save new Mobile Controller
var prefix ='/client';


var signup = function(){
	var token = $('input[name="_csrfToken"]').val();
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader("X-CSRF-TOKEN", token);
        }
    });
		var employeeData ={};
		employeeData["empId"]=$("input[name=empId]").val();
		employeeData["empName"]=$("input[name=empName]").val();
		employeeData["email"]=$("input[name=email]").val();
		employeeData["password"]=$("input[name=password]").val();
		employeeData["jobLevel"]=$("select[name=jobLevel]").val();
		employeeData["totalLeave"]=$("input[name=totalLeave]").val();
		employeeData["bossId"]=$("input[name=bossId]").val();
		$.ajax({
			type: 'POST',
			url:  prefix + '/signup',
			data: JSON.stringify(employeeData),
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			timeout: 100000,
			async: true,
			success: function(result) {
				alert("Success : "+result.data);
				location.reload();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Something went wrong. Please try again!");
			}
	   });
}

var applyeLeave = function(){
	var token = $('meta[name="_csrf"]').attr('content');
	$.ajaxSetup({
		beforeSend: function(xhr) {
			xhr.setRequestHeader("X-CSRF-TOKEN", token);
		}
	});
	var employeeData ={};
	employeeData["empId"]=$("#actionTakerId").html()
	employeeData["leaveDays"]=$("select[name=leaveDays]").val();
	employeeData["totalLeave"]=$("#totalLeave").html();
	$.ajax({
		type: 'POST',
		url:  prefix + '/secure/apply-leave',
		data: JSON.stringify(employeeData),
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		timeout: 100000,
		async: true,
		success: function(result) {
			alert("Success : "+result.data);
			window.location='/client/secure/welcome';
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("Something went woring. Please try again!");
		}
	});
}


var takeAction = function(action,index,object){
	var token = $('meta[name="_csrf"]').attr('content');
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader("X-CSRF-TOKEN", token);
        }
    });
	
	var mailObj = {};
	mailObj["applicantId"]=$("#empId_"+index).html();
	mailObj["applicantEmailId"]=$("#empEmailId_"+index).html();
	mailObj["totalLeave"]=$("#totalLeave_"+index).html();
	mailObj["leaveDays"]=$("#leaveDays_"+index).html();
	mailObj["bossId"]=$("#bossId_"+index).html();
	mailObj["actionTakerId"]=$("#actionTakerId").html();
	mailObj["actionTakerEmailId"]=$("#loggedInEmailId").html();
	var url;
	if(action==='reject'){
		url='/secure/rejectLeave';
	}else{
		url='/secure/approveLeave';
	}
	$.ajax({
		type: 'PUT',
		url:  prefix + url,
		data: JSON.stringify(mailObj),
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		timeout: 100000,
		async: true,
		success: function(result) {
			alert("Success : "+result.data);
			window.location='/client/secure/welcome';
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.status + ' ' + jqXHR.responseText);
		},
		done: function(e){
			alert("Done!");
		}
   });
}


var applyNewLeave = function(){
	var token = $('meta[name="_csrf"]').attr('content');
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader("X-CSRF-TOKEN", token);
        }
    });
		var employeeData ={};
		employeeData["empId"]=$("#actionTakerId").html();
		
		$.ajax({
			type: 'POST',
			url:  prefix + '/secure/applyNewLeave',
			data: JSON.stringify(employeeData),
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			timeout: 100000,
			async: true,
			success: function(result) {
				alert("Success : "+result.data);
				window.location='/client/secure/welcome';
				location.reload();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("Something went wrong. Please try again!");
			}
	   });
}



var showDetail = function(){
	
	$("#show").css("display","none");
	$("#hide").css("display","initial");
	$("#below").css("display","block");
}

var hideDetail = function(){
	$("#hide").css("display","none");
	$("#below").css("display","none");
	$("#show").css("display","initial");
}