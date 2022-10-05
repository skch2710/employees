package com.springboot.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

	private Long empId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String salary;
	private String address;
}
