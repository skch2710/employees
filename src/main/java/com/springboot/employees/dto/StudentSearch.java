package com.springboot.employees.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentSearch extends Pagination {

	private List<String> emailId;
	private List<Long> studentId;
	private String fullName;
	private String dob;
	private String fromDob;
	private String toDob;
	private String fromDate;
	private String toDate;
	private String salary;
	private String fromSalary;
	private String toSalary;
	
}
