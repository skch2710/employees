package com.springboot.employees.cotroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentSearch;
import com.springboot.employees.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	/**
	 * getting Students Details using Native Query
	 * Pagination and Sort
	 * 
	 * @param StudentSearch studentSearch
	 * @return Result the result
	 */
	@PostMapping("/students")
	public ResponseEntity<?> getStudents(@RequestBody StudentSearch studentSearch) {
		return ResponseEntity.ok(studentService.getStudents(studentSearch));
	}

	/**
	 * Saving Students
	 * 
	 * @param studentDTOs
	 * @return Result the result
	 */
	@PostMapping("/save")
	public ResponseEntity<Result> saveStudents(@RequestBody List<StudentDTO> studentDTOs){
		return ResponseEntity.ok(studentService.saveStudent(studentDTOs));
	}
}
