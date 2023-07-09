package com.springboot.employees.cotroller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentDropSearch;
import com.springboot.employees.dto.StudentSearch;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.service.StudentService;
import com.springboot.employees.util.Utility;

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
	
	@PostMapping("/drop-search")
	public ResponseEntity<?> searchDropStudent(@RequestBody StudentDropSearch search) {
		return ResponseEntity.ok(studentService.searchDropStudent(search));
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
	
	@PostMapping("/download-student-excel")
	public ResponseEntity<?> downloadStudentExcel(@RequestBody StudentSearch search) {
		try {
			ByteArrayOutputStream outputStream = studentService.downloadStudentExcel(search);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.attachment().filename("Student Sheet.xlsx").build());

			InputStreamResource inputStreamResource = new InputStreamResource(
					new ByteArrayInputStream(outputStream.toByteArray()));

			// Flush the output stream
			outputStream.flush();

			return ResponseEntity.ok().headers(headers).body(inputStreamResource);
		} catch (IOException e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/zip-download")
	public ResponseEntity<?> downloadZip(@RequestBody StudentSearch search) throws DocumentException {
		try {
			String zipFileName = "files.zip";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", zipFileName);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(baos))) {
				Utility.createFileToZip(zipOutputStream, "example.xlsx", studentService.downloadStudentExcel(search));
				Utility.createFileToZip(zipOutputStream, "example1.pdf", studentService.generatePdfWithTable(search));
				// Add more files to the zip if needed
				zipOutputStream.finish();
			}

			InputStreamResource inputStreamResource = new InputStreamResource(
					new ByteArrayInputStream(baos.toByteArray()));
			return ResponseEntity.ok().headers(headers).body(inputStreamResource);
		} catch (IOException e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/generate-pdf")
	public ResponseEntity<?> generatePdf(@RequestBody StudentSearch search) throws DocumentException {
		try {
			ByteArrayOutputStream outputStream = studentService.generatePdfWithTable(search);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Student Data.pdf");

			InputStreamResource inputStreamResource = new InputStreamResource(
					new ByteArrayInputStream(outputStream.toByteArray()));

			outputStream.flush();// Flush the output stream

			return ResponseEntity.ok().headers(headers).body(inputStreamResource);
		} catch (IOException e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
