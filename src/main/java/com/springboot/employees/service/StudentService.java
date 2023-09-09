package com.springboot.employees.service;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentDropSearch;
import com.springboot.employees.dto.StudentSearch;

public interface StudentService {

	Result getStudents(StudentSearch search);

	Result saveStudent(List<StudentDTO> studentDTOs);
	
	ByteArrayOutputStream generatePdfWithTable(StudentSearch search);
	
	ByteArrayOutputStream downloadStudentExcel(StudentSearch search);
	
	Result searchDropStudent(StudentDropSearch search);
	
	Result batchUploadExcel(MultipartFile file) throws ParseException;
	
}
