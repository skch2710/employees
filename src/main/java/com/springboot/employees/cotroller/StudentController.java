package com.springboot.employees.cotroller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentDropSearch;
import com.springboot.employees.dto.StudentSearch;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.service.StudentService;
import com.springboot.employees.util.Utility;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
//@RequestMapping("/api/v1/student")
@RequestMapping("/student")
//@SecurityRequirement(name = "bearerAuth")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<Result> findById(@PathVariable("id") Long id) {
		Result result = studentService.getStudentById(id);
		return ResponseEntity.ok(result);
	}

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
//	@PreAuthorize("hasAnyAuthority('Super User','Admin')")
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
//	@PreAuthorize("hasAnyAuthority('Super User')")
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
	@PreAuthorize("hasAnyAuthority('Super User')")
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
	
	/**
	 * This API is Upload Excel file Records
	 * 
	 * @param file
	 * @return Result response
	 * @throws ParseException
	 */
	@PostMapping(path = "/batch-upload", consumes = { "application/json", "multipart/form-data" })
	public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) throws ParseException {
		return ResponseEntity.ok(studentService.batchUploadExcel(file));
	}
	
	@PostMapping("/download-error-excel")
	public ResponseEntity<?> downloadError(@RequestBody List<StudentDTO> studentDTOs) {
		try {
			ByteArrayOutputStream outputStream = studentService.downloadError(studentDTOs);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.attachment().filename("Student Error Sheet.xlsx").build());

			InputStreamResource inputStreamResource = new InputStreamResource(
					new ByteArrayInputStream(outputStream.toByteArray()));

			// Flush the output stream
			outputStream.flush();

			return ResponseEntity.ok().headers(headers).body(inputStreamResource);
		} catch (IOException e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
