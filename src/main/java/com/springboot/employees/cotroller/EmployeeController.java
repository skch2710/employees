package com.springboot.employees.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeListSearch;
import com.springboot.employees.dto.EmployeeSearch;
import com.springboot.employees.dto.Pagination;
import com.springboot.employees.dto.Result;
import com.springboot.employees.service.EmployeeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/employee")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/findAll")
	public ResponseEntity<Result> findAll() {
		Result result = employeeService.findAll();
		return ResponseEntity.ok(result);
	}

//	@GetMapping("/findAllPagination")
//	public ResponseEntity<Result> findAll(Pagination pagination) {
//		Result result = employeeService.findAll(pagination);
//		return ResponseEntity.ok(result);
//	}

	@GetMapping("/findById/{empid}")
	public ResponseEntity<Result> findById(@PathVariable Long empid) {
		Result result = employeeService.findById(empid);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/save")
	public ResponseEntity<Result> saveEmployees(@RequestBody EmployeeDTO empData) {
		return new ResponseEntity<Result>(employeeService.save(empData), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Result> updateEmployees(@RequestBody EmployeeDTO empData) {
		return new ResponseEntity<Result>(employeeService.update(empData), HttpStatus.OK);
	}

	@DeleteMapping("/deleteById/{emailId}")
	public ResponseEntity<Result> deleteById(@PathVariable String emailId) {
		Result result = employeeService.deleteById(emailId);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 
	 * @param emailid
	 * @return
	 */
	@GetMapping("/findByEmailId/{emailid}")
	public ResponseEntity<Result> findByEmailId(@PathVariable String emailid) {
		Result result = employeeService.findByEmailId(emailid);
		return ResponseEntity.ok(result);
	}

	/**
	 * Email exist.
	 *
	 * @param authorization the authorization
	 * @param emailID       the email ID
	 * @return the response entity
	 */
	@GetMapping("/emailexist/{email}")
	public ResponseEntity<?> emailexist(@RequestHeader(required = false, value = "Authorization") String authorization,
			@PathVariable("email") String emailID) {
		return ResponseEntity.ok(employeeService.emailExist(emailID));

	}

	@GetMapping("/searchByStartWith/{startWith}")
	public ResponseEntity<?> emailexist(@PathVariable("startWith") String startWith) {
		return ResponseEntity.ok(employeeService.findByStartWith(startWith));

	}

	/**
	 * Search by Email
	 * 
	 * @param mailchech
	 * @return Result result
	 */
	@GetMapping("/mailChech/{mailchech}")
	public ResponseEntity<?> startWith(@PathVariable("mailchech") String mailchech) {
		return ResponseEntity.ok(employeeService.mailChech(mailchech));

	}

	/**
	 * Search Employees
	 *
	 * @param employeeSearch the employee search
	 * @return the response entity
	 */
	@PostMapping("/filterSearch")
	public ResponseEntity<Result> filterSearch(@RequestBody EmployeeSearch employeeSearch) {
		Result result = employeeService.filterSearch(employeeSearch);
		return ResponseEntity.ok(result);
	}

	/**
	 * get Employees by List of Id's
	 *
	 * @param employeeSearch the employee search
	 * @return the response entity
	 */
	@PostMapping("/listEmpById")
	public ResponseEntity<?> getByListEmpId(@RequestBody EmployeeListSearch employeeListSearch) {
		return ResponseEntity.ok(employeeService.findByListEmpId(employeeListSearch));

	}

	/**
	 * Get By Email And FirstName.
	 *
	 * @param emailID and FirstName.
	 * @return the response entity
	 */
	@GetMapping("/employeeDetailsBy/{emailId}/{firstName}")
	public ResponseEntity<?> findByEmailIdAndFirstName(@PathVariable String emailId, @PathVariable String firstName) {
		return ResponseEntity.ok(employeeService.findByEmailIdAndFirstName(emailId, firstName));

	}
}
