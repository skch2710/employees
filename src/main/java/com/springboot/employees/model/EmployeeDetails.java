package com.springboot.employees.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name="employee_details",schema="employees")
//@JsonIgnoreProperties(ignoreUnknown = true, value = {"employee"})
public class EmployeeDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empDetailsId;
	
	private BigDecimal salary;
	
	private String address;
	
	@JsonIgnore
	@OneToOne(mappedBy = "employeeDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Employee employee;
}
