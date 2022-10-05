package com.springboot.employees.dto;

import com.springboot.employees.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailsDTO {
	
	private Long empDetailsId;
	private Double salary;
	private String address;
	private Long empId;
	private String firstName;
	private String lastName;
	private String emailId;

}
