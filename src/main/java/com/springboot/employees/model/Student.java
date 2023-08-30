package com.springboot.employees.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "students", schema = "employees")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentId;

	private String fullName;

	private String emailId;
	
	private Date dob;

	private String mobileNumber;

	private BigDecimal salary;
	
	private Date fromDate;
	
	private Date toDate;
}
