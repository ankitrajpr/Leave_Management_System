package com.example.spring.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.example.spring.bean.Employee;


@Service	//For declear that class is in service layer (Autowire)
@EnableCouchbaseRepositories(basePackages= {"com.example.spring"})
public class EmployeeService implements IEmployeeService{

	@Autowired
	IEmployeeRepo empRepo;
	
	private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Override
	public Employee getEmployeeByEmpId(long empId) {//This method will call couch base to get data from database by empId
		System.out.println("Find By Id "+empId);
		logger.info("getEmployeeByEmpId Service : Started");
		N1qlQuery selectWithId = N1qlQuery.simple("select * from `leave` where empId="+empId+";");//This is to form N1QLQuery from String //This is the query which we will execute to get employee where empId is mentioned 
		N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(selectWithId);//It will execute the above query in couchbase to return N1QLQueryResult.
		System.out.println(queryResult);
		JsonObject empObj = queryResult.allRows().get(0).value().getObject("leave");//To get all records and then get all from index 0 then get its value with object name leave in JsonObject
		Employee employee = new Employee();//Now we have JsonObject and we can easily convert it into employee Object
		employee.setEmpId(empObj.getInt("empId"));
		employee.setEmpName(empObj.getString("empName"));
		employee.setEmail(empObj.getString("email"));
		employee.setPassword(empObj.getString("password"));
		employee.setRole(empObj.getString("role"));
		employee.setLeaveDays(empObj.getInt("leaveDays"));
		employee.setJobLevel(empObj.getInt("jobLevel"));
		employee.setBossId(empObj.getInt("bossId"));
		employee.setTotalLeave(empObj.getInt("totalLeave"));
		employee.setLeaveAllocation(empObj.getBoolean("leaveAllocation"));
		employee.setEnable(empObj.getBoolean("enable"));
		employee.setAppliedForLeave(empObj.getBoolean("appliedForLeave"));
		
		return employee;//return Employee object
	}
	
	@Override
	public Optional<Employee> getEmployeeByDocumentId(String docId) {//This is the method which will return documnet instead of records. Documents will have all records in single document with specific id.
		logger.info("getEmployeeByDocumentId Service : Started");
		System.out.println("Find By Document Id "+docId);//
		return empRepo.findById(docId);//This is the method defined by Couchbase to find document by DocumentId
	}

	@Override
	public Employee addNewEmployee(Employee emp) {//This is the method to save new Employee
		logger.info("addNewEmployee Service : Started");
		return empRepo.save(emp);//This is defined method of couchbase to save document in same format which we have define Employee. There we have defined what should be our document Id which is mandatory to define while creating any document
	}

	@Override
	public boolean deleteEmployeeByEmpId(long empId) {//This is use to delete an record from document with some condition as like normal sql
		logger.info("deleteEmployeeByEmpId Service : Started");
		if(validateEmployeeByEmpId(empId)) {//checking if that data exist in out bucket or not before deleting
			String deleteQueryString = "delete from `leave`where empId="+empId;//This is query to delete by Id
			N1qlQuery deleteRecord = N1qlQuery.simple(deleteQueryString);//converting to N1QlQuery
			N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(deleteRecord);//Executing query and returning result
			System.out.println(queryResult);
			if("success".equalsIgnoreCase(queryResult.status()))//if status is success means deleted successfully
				return true;
			else
				return false;
		}else {
			return false;
		}
	}

	@Override
	public boolean validateEmployeeByEmpId(long empId) {//This is the method to check if this empId is exist in the connected bucket 
		logger.info("validateEmployeeByEmpId Service : Started");
		if(empId!=0) {
			Employee employeeObj = getEmployeeByEmpId(empId);	//if this will return any object of employee mean data is there in our bucket
			//Means Result has data
			//if(result!= null && "success".equalsIgnoreCase(result.status())) {
			if(employeeObj != null && employeeObj.getEmpId() != 0) {
				return true;
			}else
				return false;
		}else {
			return false;
		}
	}
	
	
	@Override
	public boolean updateEmployeeByEmpId(long empId) {//This is the method to employee with help of employee id as we have distinct employee id
		logger.info("updateEmployeeByEmpId Service : Started");
		if(empId!=0) {
			String updateQueryString = "Update `leave` set appliedForLeave = false, leaveAllocation = false  where empId = "+empId;//This is the query to update document record where empid will match
			N1qlQuery updateLeave = N1qlQuery.simple(updateQueryString);//convert string to N1QLQuery
			N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(updateLeave);//execute query to couchbase and return data
			System.out.println(queryResult);
			if("success".equalsIgnoreCase(queryResult.status()))//if statsu is success means successfully executed
				return true;
			else
				return false;
		}
		return false;
	}
	
	@Override
	public boolean applyLeave(Employee emp) {//this is also to update data in documents of bucket
		logger.info("applyLeave Service : Started");
		if(emp!=null && emp.getEmpId()!=0) {
			if((emp.getTotalLeave()-emp.getLeaveDays())>=0){//this is condition we are checking for safer side
				String updateQueryString = "Update `leave` set leaveDays = "+emp.getLeaveDays()+" , leaveAppliedDate = "+emp.getLeaveAppliedDate()+", appliedForLeave = true where empId = "+emp.getEmpId();//this is the query which we will execute
				N1qlQuery updateLeave = N1qlQuery.simple(updateQueryString);//convert to N1QLQuery 
				N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(updateLeave);//execute the query
				System.out.println(queryResult);
				if("success".equalsIgnoreCase(queryResult.status()))//if success
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	@Override
	public boolean approveLeave(Employee emp) {//This is the method to update couchbase data
		logger.info("approveLeave Service : Started");
		if(emp!=null && emp.getEmpId()!=0) {//checking condition
			emp.setTotalLeave(emp.getTotalLeave()-emp.getLeaveDays());//if we r approving leavin means total leaves will get reducs by how much he/she has applied
			if(emp.getTotalLeave()>0){
				String approveQueryString = "Update `leave` set totalLeave = "+emp.getTotalLeave()+" , leaveAllocation = true where empId = "+emp.getEmpId();//couchbase query to update
				N1qlQuery updateApproveLeave = N1qlQuery.simple(approveQueryString);//convert to query
				N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(updateApproveLeave);//execute query return result
				System.out.println(queryResult);
				if("success".equalsIgnoreCase(queryResult.status()))//check if success
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	//For Rejection of Leave
	@Override
	public boolean rejectLeave(Employee emp) {//This is the method which will update when it will get reject
		logger.info("rejectLeave Service : Started");
		if(emp!=null && emp.getEmpId()!=0) {//checking for safer side.
			String rejectQueryString = "Update `leave` set leaveDays = 0, appliedForLeave = false where empId = "+emp.getEmpId();//just making leave day = 0 and appliedForLeave= false
			N1qlQuery rejectUpdateLeave = N1qlQuery.simple(rejectQueryString);////convert to N1QLQuery
			N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(rejectUpdateLeave);//execute query
			System.out.println(queryResult);
			if("success".equalsIgnoreCase(queryResult.status()))//if success
				return true;
			else
				return false;
		}
		return false;
	}
	
	@Override
	public List<Employee> getJuniorEmployee(long bossId) {//This is the method which will fetch all those employee who have applied for leave and are under loggedin User till level 2
		logger.info("getJuniorEmployee Service : Started");
		String fieldsToGet = "empId,empName,email,jobLevel,bossId,totalLeave,leaveDays,leaveAllocation,leaveAppliedDate,appliedForLeave";//these r the fields need to select from database
		String levelOneDownEmp = "SELECT "+fieldsToGet+" FROM `leave` WHERE bossId="+bossId;//selcet all employee who are directly under him. means whos boss Id is his employee Id
		
		List<Employee> empAppledForLeave = new ArrayList<>();//To Store all those employee who are on leave
		
		List<Employee> leveOneDownEmpAppledLeave = new ArrayList<>();//To store level 1 down employee
		List<Employee> leveTwoDownEmpAppledLeave = new ArrayList<>();//To store level2 down employee
		
		List<Long> leveOneDownEmpId = new ArrayList<>();//To store those employeeId who are directly under logged in user 
		
		logger.info("getJuniorEmployee Service : Getting 1 level down employee list.");
		N1qlQuery oneLevelQuery = N1qlQuery.simple(levelOneDownEmp);//convert level 1 down query
		N1qlQueryResult firstQueryResult = empRepo.getCouchbaseOperations().queryN1QL(oneLevelQuery);//execute
		//This query will give all the employee whose boss is current user.
		for(N1qlQueryRow element : firstQueryResult.allRows()) {//Now iterate it to convert all into proper Employee Object 
			JsonObject firstResultObj = element.value();//create JsoNObject then convert to employee Object
			Employee e = customObjectParser(firstResultObj);//get Employee from Json object
			//Adding for further use
			leveOneDownEmpId.add(e.getEmpId());//Storing level 1 down employee employeeId in this list whci we will use to fetch leve 2 down employee
			if(e.isAppliedForLeave())//check if this employee has applied for leave if yess store it to return else neglect
				leveOneDownEmpAppledLeave.add(e);
			
		}
		if(leveOneDownEmpId.size()>0) {//Now if there is any employee who are directly under current user then proceed further to check for level 2 down employee
			String levelTwoDownEmp = "SELECT "+fieldsToGet+" FROM `leave` WHERE bossId IN "+leveOneDownEmpId;//This is the query which will fetch all employee who have bossId as any of the employeeIs from This list
			
			logger.info("getJuniorEmployee Service : Getting 2 level down employee list.");
			N1qlQuery twoLevelQuery = N1qlQuery.simple(levelTwoDownEmp);//convert to query
			N1qlQueryResult secondQueryResult = empRepo.getCouchbaseOperations().queryN1QL(twoLevelQuery);//execute query
			for(N1qlQueryRow element : secondQueryResult) {//Agian iterate it to convert it into proper Employee Object
				
				JsonObject secondResultObj = element.value();//convert to JsonObject
				Employee e = customObjectParser(secondResultObj);//Convert To Employee Object
				if(e.isAppliedForLeave())//now check who among this have applied for leave and add thos level 2 down appled for leav list.
					leveTwoDownEmpAppledLeave.add(e);
			}
		}
		
		empAppledForLeave.addAll(leveOneDownEmpAppledLeave);//Add to final list which will return by api
		empAppledForLeave.addAll(leveTwoDownEmpAppledLeave);//Add to final list which will return by api
		
		System.out.println(empAppledForLeave);
		
		return empAppledForLeave;
	}
	
	@Override
	public Employee customObjectParser(JsonObject leaveObj/*employee Object*/) {//This is the method which will convert JsnObject to employee Object
		logger.info("customObjectParser Service : Started");
		Employee employee = new Employee();
		if(!leaveObj.isEmpty()) {
			employee.setEmpId(leaveObj.getLong("empId"));
			employee.setEmpName(leaveObj.getString("empName"));
			employee.setEmail(leaveObj.getString("email"));
			employee.setLeaveDays(leaveObj.getInt("leaveDays"));
			employee.setJobLevel(leaveObj.getInt("jobLevel"));
			employee.setBossId(leaveObj.getInt("bossId"));
			employee.setTotalLeave(leaveObj.getInt("totalLeave"));
			employee.setLeaveAllocation(leaveObj.getBoolean("leaveAllocation"));
			employee.setLeaveAppliedDate(leaveObj.getLong("leaveAppliedDate"));
			employee.setAppliedForLeave(leaveObj.getBoolean("appliedForLeave"));
		}
		return employee;
	}
	 
	@Scheduled(cron = "0 0 1 1 * ?") //minute hour date month(every year on 1 Jan at 00:00 ) * = any
	public void scheduleTaskWithCronExpression() {
		long yearlyLeaveDaysForAll = 20;
		System.err.println("cron running of Leave"+System.currentTimeMillis());
	    logger.info("Cron Task :: Execution Time - {}"+new Date().getTime());
	    String getAllRecordsQuery = "select * from `leave`";//This query will fetch all employee
	    N1qlQuery fetchAllQuery = N1qlQuery.simple(getAllRecordsQuery);//convert to query
	    N1qlQueryResult queryResult = empRepo.getCouchbaseOperations().queryN1QL(fetchAllQuery);//fetch all employee
	    for(N1qlQueryRow element : queryResult.allRows()) {//for echa employee
	    	JsonObject leaveObj = element.value();//convert String to JsonObject
	    	JsonObject empObj = leaveObj.getObject("leave");//To employee Object
	    	logger.info("updating leave days annually for empId "+empObj.getLong("empId"));
	    	String upateAnnualLeave = "Update `leave` set totalLeave = "+(empObj.getLong("totalLeave")+yearlyLeaveDaysForAll)+" where empId = "+empObj.getLong("empId");//This is the query which will add remaining leave and next year leave and update it back to couchbase db
	    	N1qlQuery updateEmployeeDaysLeave = N1qlQuery.simple(upateAnnualLeave);//convert to query
	    	empRepo.getCouchbaseOperations().queryN1QL(updateEmployeeDaysLeave);//execute query
	    }
	    logger.info("Cron Job : Successfully Updated all records");
	}

}
