package com.springboot.employees.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "students", schema = "public")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentId;

	private String fullName;

	private String emailId;
	
	private LocalDate dob;

	private String mobileNumber;

	private BigDecimal salary;
	
	private LocalDate fromDate;
	
	private LocalDate toDate;
}
