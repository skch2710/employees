package com.springboot.employees.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.employees.model.Employee;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Long> {

	List<Employee> findByEmailId(String email);

	@Query("SELECT e FROM Employee e WHERE e.firstName LIKE :firstName%")
	List<Employee> findByStartWith(@Param("firstName") String firstName);

	@Query(value ="SELECT * FROM employee WHERE email_id LIKE %:mailchech%" , nativeQuery = true)
	List<Employee> mailChech(@Param("mailchech") String mailchech);
	
	Page<Employee> findAll(Pageable pageable);
}
