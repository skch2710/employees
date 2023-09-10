package com.springboot.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

	
	private Long studentId;

	private String fullName;

	private String emailId;
	
	private String dob;

	private String mobileNumber;

	private String salary;
	
	private String fromDate;
	
	private String toDate;
	
	private String errorMessage;

}
