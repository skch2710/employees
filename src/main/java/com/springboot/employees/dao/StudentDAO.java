package com.springboot.employees.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.employees.model.Employee;
import com.springboot.employees.model.Student;

@Repository
public interface StudentDAO extends JpaRepository<Student, Long> {
	
	@Query("SELECT s FROM Student s WHERE emailId in ?1")
	Page<Student> getStudents(List<String> emailId,Specification<Employee> spec, Pageable pageable);

	@Query("SELECT s FROM Student s WHERE emailId in ?1")
	List<Student> getStudents(List<String> emailId,Specification<Employee> spec, Sort sort);
	
	Page<Student> findAll(Specification<Student> spec, Pageable pageable);

	List<Student> findAll(Specification<Student> spec, Sort sort);
	
	Student findByEmailIdIgnoreCase(String emailId);
	
	Student findByStudentId(Long id);
}
