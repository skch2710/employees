package com.springboot.employees.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.employees.model.EmployeeDetails;

public interface EmployeeDetailsDAO extends JpaRepository<EmployeeDetails, Long> {

}
