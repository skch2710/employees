package com.springboot.employees.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearch extends Pagination {

	private List<Long> empId;
//	private Long empId;
	private String firstName;
	private String emailId;

}
