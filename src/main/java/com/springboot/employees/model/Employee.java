package com.springboot.employees.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="employee",schema="employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empId;
	
//	@Column(name="first_name")
	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="emp_details_id")
	private EmployeeDetails employeeDetails;
}
