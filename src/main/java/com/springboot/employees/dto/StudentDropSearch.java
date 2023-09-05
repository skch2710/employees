package com.springboot.employees.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDropSearch {
	
	private List<Long> studentId;
	private String fullName;
	private String dob;
	
}
