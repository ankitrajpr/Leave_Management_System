package com.example.spring.service;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.bean.Employee;

@Repository//This is defined as repositry from here only we got all couchbase default metod like save() findById() etc
public interface IEmployeeRepo extends CouchbaseRepository<Employee, String>{

}
