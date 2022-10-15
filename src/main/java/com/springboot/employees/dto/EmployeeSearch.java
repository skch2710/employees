package com.springboot.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearch extends Pagination {

	private Long empId;
	private String firstName;
	private String lastName;
	private String emailId;

}
