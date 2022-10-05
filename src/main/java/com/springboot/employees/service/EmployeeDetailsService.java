package com.springboot.employees.service;

import com.springboot.employees.model.Result;

public interface EmployeeDetailsService {
	
	public Result findAll();
	public Result findById(Long id);
}
