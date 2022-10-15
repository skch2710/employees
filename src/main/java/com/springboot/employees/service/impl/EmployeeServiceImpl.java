package com.springboot.employees.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.employees.common.Constants;
import com.springboot.employees.common.ConstantsEnum;
import com.springboot.employees.dao.EmployeeDAO;
import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeSearch;
import com.springboot.employees.dto.Pagination;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.SearchResult;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.mapper.ObjectMapper;
import com.springboot.employees.model.Employee;
import com.springboot.employees.model.EmployeeDetails;
import com.springboot.employees.service.EmployeeService;
import com.springboot.employees.specs.GenericSpecification;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;

	private ObjectMapper MAPPER = ObjectMapper.INSTANCE;

	@Override
	public Result findAll() {

		Result result = null;
		try {

			List<EmployeeDTO> employeeDTOs = MAPPER.fromEmployeeModel(employeeDAO.findAll());
			result = new Result(employeeDTOs);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("fetch successfully.");

		} catch (Exception e) {
			throw new CustomException("Fetching Error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

	@Override
	public Result findById(Long id) {

		Result result = new Result();
		try {
			EmployeeDTO employeeDTO = MAPPER.fromEmployeeModel(employeeDAO.findById(id).orElse(null));
			if (employeeDTO == null) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("Id not Found.");
			} else {
				System.out.println(ConstantsEnum.SATHISH+" "+ConstantsEnum.KUMAR+" : \n"+employeeDTO);
				System.out.println(Constants.SATHISH_KUMAR);
				result.setData(employeeDTO);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Fetching is done.");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//			throw new CustomException("Id Not Found.", HttpStatus.NOT_FOUND);
		}

		return result;
	}

	@Override
	public Result save(EmployeeDTO employeeDTO) {
		Result result = null;
		try {

			Employee employee = new Employee();

			employee.setFirstName(employeeDTO.getFirstName());
			employee.setLastName(employeeDTO.getLastName());
			employee.setEmailId(employeeDTO.getEmailId());

			EmployeeDetails employeeDetails = new EmployeeDetails();
			employeeDetails.setSalary(Double.valueOf(employeeDTO.getSalary()));
			employeeDetails.setAddress(employeeDTO.getAddress());

			employee.setEmployeeDetails(employeeDetails);

			EmployeeDTO emps = MAPPER.fromEmployeeModel(employeeDAO.save(employee));
			result = new Result(emps);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("Data added successfully.");

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@Override
	public Result update(EmployeeDTO employeeDTO) {
		Result result = new Result();
		try {
			Employee employee = employeeDAO.findById(employeeDTO.getEmpId()).orElse(null);

			if (employee == null) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("Employee not found");
			} else {
				employee.setFirstName(employeeDTO.getFirstName());
				employee.setLastName(employeeDTO.getLastName());
				employee.setEmailId(employeeDTO.getEmailId());

				EmployeeDetails employeeDetails = employee.getEmployeeDetails();
				employeeDetails.setSalary(Double.valueOf(employeeDTO.getSalary()));
				employeeDetails.setAddress(employeeDTO.getAddress());
				employee.setEmployeeDetails(employeeDetails);

				EmployeeDTO updateEmployee = MAPPER.fromEmployeeModel(employeeDAO.save(employee));

				result.setData(updateEmployee);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Data updated successfully.");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@Override
	public Result deleteById(Long id) {
		Result result = new Result();
		try {

			employeeDAO.deleteById(id);
//			result = new Result(employee);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("delete successfully.");
		} catch (Exception e) {
			throw new CustomException("Id Not Found.", HttpStatus.NOT_FOUND);
		}

		return result;
	}

	@Override
	public Result findByEmailId(String email) {
		Result result = new Result();
		try {

			List<EmployeeDTO> employees = MAPPER.fromEmployeeModel(employeeDAO.findByEmailId(email));
			if (employees != null && employees.size() == 0) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("Email Id Not Found.");
			} else {
				result.setData(employees);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("find successfully.");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

	/**
	 * Email exist.
	 *
	 * @param emailID the email ID
	 * @return the result
	 */
	@Override
	public Result emailExist(String email) {
		Result result = new Result();
		List<Employee> employees = employeeDAO.findByEmailId(email);
		if (employees != null && employees.size() == 0) {
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("Email Id doesn't exist");
		} else {
			result.setStatusCode(HttpStatus.NOT_FOUND.value());
			result.setErrorMessage("Email Id is already exists");
		}

		return result;
	}

	@Override
	public Result findByStartWith(String firstName) {
		Result result = new Result();
		List<EmployeeDTO> employees = MAPPER.fromEmployeeModel(employeeDAO.findByStartWith(firstName));
		if (employees != null && employees.size() == 0) {
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("doesn't exist");
		} else {
			result = new Result(employees);
			result.setStatusCode(HttpStatus.NOT_FOUND.value());
			result.setErrorMessage("Fetching success.");

		}

		return result;
	}

	@Override
	public Result mailChech(String mailchech) {
		Result result = new Result();
		try {
			List<EmployeeDTO> employees = MAPPER.fromEmployeeModel(employeeDAO.mailChech(mailchech));
			if (employees != null && employees.size() == 0) {
				result.setStatusCode(HttpStatus.BAD_REQUEST.value());
				result.setErrorMessage("doesn't exist");
			} else {
				result.setData(employees);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Fetching success.");

			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

	@Override
	public Result findAll(Pagination pagination) {

		Result result = new Result();

		try {

			if (pagination.getSortBy() == null || "".equalsIgnoreCase(pagination.getSortBy())) {
				pagination.setSortBy("empId");
			}

			Sort sort = null;

			if (pagination.getSortOrder() == null || "".equalsIgnoreCase(pagination.getSortOrder())
					|| pagination.getSortOrder().equalsIgnoreCase("asc")) {
				sort = Sort.by(pagination.getSortBy()).ascending();
			} else if (pagination.getSortOrder().equalsIgnoreCase("desc")) {
				sort = Sort.by(pagination.getSortBy()).descending();
			}

			Pageable paging = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sort);

			Page<Employee> employees = employeeDAO.findAll(paging);

			if (employees.hasContent()) {

				SearchResult<EmployeeDTO> searchResult = GenericSpecification.getPaginationDetails(employees,
						EmployeeDTO.class);

				searchResult.setContent(MAPPER.fromEmployeeModel(employees.getContent()));

				result.setData(searchResult);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("getting success.");
			} else {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("fetch failed No records found.");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

	/**
	 * Employee Data based on search field.
	 * 
	 * @param EmployeeSearch
	 * 
	 * @return Employees Data
	 */
	@Override
	public Result filterSearch(EmployeeSearch employeeSearch) {
		return null;
	}
}
