package com.springboot.employees.service;

import java.util.List;

import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentSearch;

public interface StudentService {

	Result getStudents(StudentSearch studentSearch);

	Result saveStudent(List<StudentDTO> studentDTOs);

}
