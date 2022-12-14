package com.springboot.employees.service;

import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeListSearch;
import com.springboot.employees.dto.EmployeeSearch;
import com.springboot.employees.dto.Result;

public interface EmployeeService {

	public Result findAll();

//	public Result findAll(Pagination pagination);

	public Result findById(Long id);

	public Result findByEmailId(String email);

	public Result save(EmployeeDTO employeeDTO);

	public Result update(EmployeeDTO employeeDTO);

//	public Result deleteById(Long id);
	public Result deleteById(String emailId);

	/**
	 * EmailExist.
	 *
	 * @param emailID the email ID
	 * @return the result
	 */
	public Result emailExist(String email);

	public Result findByStartWith(String firstName);

	public Result mailChech(String mailchech);

	Result filterSearch(EmployeeSearch employeeSearch);

	public Result findByListEmpId(EmployeeListSearch employeeListSearch);
	
	public Result findByEmailIdAndFirstName(String emailId,String firstName);
}
