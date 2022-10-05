package com.springboot.employees.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.employees.dao.EmployeeDetailsDAO;
import com.springboot.employees.dto.EmployeeDetailsDTO;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.mapper.ObjectMapper;
import com.springboot.employees.model.Result;
import com.springboot.employees.service.EmployeeDetailsService;

@Service
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

	@Autowired
	private EmployeeDetailsDAO employeeDetailsDAO;
	
	private ObjectMapper MAPPER = ObjectMapper.INSTANCE;
	
	@Override
	public Result findAll() {

		Result result = new Result();

		try {

			List<EmployeeDetailsDTO> employeeDetailsDTOs = MAPPER.fromEmployeeDetailsModel(employeeDetailsDAO.findAll());

			if (employeeDetailsDTOs != null && employeeDetailsDTOs.size() == 0) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("fetchng Error.");
			} else {
				result.setData(employeeDetailsDTOs);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("fetching successfully.");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return result;
	}

	@Override
	public Result findById(Long id) {
		Result result = new Result();
		try {
			EmployeeDetailsDTO employeeDetails = MAPPER.fromEmployeeDetailsModel(employeeDetailsDAO.findById(id).orElse(null));
			if (employeeDetails == null) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("Id not Found.");
			} else {
				result.setData(employeeDetails);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Fetching is done.");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

}
