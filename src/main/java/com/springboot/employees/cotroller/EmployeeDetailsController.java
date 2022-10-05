package com.springboot.employees.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.employees.model.Result;
import com.springboot.employees.service.EmployeeDetailsService;

@RestController
@RequestMapping("/api/employeeDetails")
public class EmployeeDetailsController {

	@Autowired
	private EmployeeDetailsService employeeDetailsService;

	@GetMapping("/getAll")
	public ResponseEntity<Result> getAll() {

		Result result = employeeDetailsService.findAll();

		return ResponseEntity.ok(result);
	}

	@GetMapping("/findById/{empDeailsid}")
	public ResponseEntity<Result> findById(@PathVariable Long empDeailsid) {
		Result result = employeeDetailsService.findById(empDeailsid);
		return ResponseEntity.ok(result);
	}
}
