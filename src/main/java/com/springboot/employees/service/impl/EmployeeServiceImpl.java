package com.springboot.employees.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.employees.common.Constants;
import com.springboot.employees.common.ConstantsEnum;
import com.springboot.employees.dao.EmployeeDAO;
import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeListSearch;
import com.springboot.employees.dto.EmployeeSearch;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.SearchResult;
import com.springboot.employees.email.EmailService;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.mapper.ObjectMapper;
import com.springboot.employees.model.Employee;
import com.springboot.employees.model.EmployeeDetails;
import com.springboot.employees.service.EmployeeService;
import com.springboot.employees.specs.GenericSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private EmailService emailService;

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
				System.out.println(ConstantsEnum.SATHISH + " " + ConstantsEnum.KUMAR + " : \n" + employeeDTO);
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

			Map<String, Object> model = new HashMap<>();
			model.put("fullName", emps.getFirstName() + " " + emps.getLastName());
			emailService.sendEmailWelcome(model, emps.getEmailId());

			result = new Result(emps);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("Data added successfully and Send Emaail.");

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
			List<EmployeeDTO> employees = MAPPER.fromEmployeeModel(employeeDAO.findByEmailIdContainingIgnoreCase(mailchech));
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

//	@Override
//	public Result findAll(Pagination pagination) {
//
//		Result result = new Result();
//
//		try {
//
//			if (pagination.getSortBy() == null || "".equalsIgnoreCase(pagination.getSortBy())) {
//				pagination.setSortBy("empId");
//			}
//
//			Sort sort = null;
//
//			if (pagination.getSortOrder() == null || "".equalsIgnoreCase(pagination.getSortOrder())
//					|| pagination.getSortOrder().equalsIgnoreCase("asc")) {
//				sort = Sort.by(pagination.getSortBy()).ascending();
//			} else if (pagination.getSortOrder().equalsIgnoreCase("desc")) {
//				sort = Sort.by(pagination.getSortBy()).descending();
//			}
//
//			Pageable paging = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sort);
//
//			Page<Employee> employees = employeeDAO.findAll(paging);
//
//			if (employees.hasContent()) {
//
//				SearchResult<EmployeeDTO> searchResult = GenericSpecification.getPaginationDetails(employees,
//						EmployeeDTO.class);
//
//				searchResult.setContent(MAPPER.fromEmployeeModel(employees.getContent()));
//
//				result.setData(searchResult);
//				result.setStatusCode(HttpStatus.OK.value());
//				result.setSuccessMessage("getting success.");
//			} else {
//				result.setStatusCode(HttpStatus.NOT_FOUND.value());
//				result.setErrorMessage("fetch failed No records found.");
//			}
//		} catch (Exception e) {
//			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return result;
//	}

	/**
	 * Employee Data based on search field. ( String - contains , Id - = )
	 * 
	 * @param employeeSearch
	 * 
	 * @return Employees Data
	 */
	@Override
	public Result filterSearch(EmployeeSearch employeeSearch) {
		Result result = null;

		try {
			Map<String, Object> filters = null;
			Map<String, Map<String, Object>> dynamicFilters = new HashMap<>();

			if (employeeSearch.getSortBy() == null || "".equalsIgnoreCase(employeeSearch.getSortBy())) {
				employeeSearch.setSortBy(Constants.FIRST_NAME);
			}

			if (employeeSearch.getColumnFilters() != null) {
				GenericSpecification.dynamicFilters(employeeSearch.getColumnFilters(), dynamicFilters);
			}

			if (employeeSearch.getFirstName() != null) {
				filters = new HashMap<String, Object>();
				filters.put(Constants.LIKE, employeeSearch.getFirstName());
				dynamicFilters.put(Constants.FIRST_NAME, filters);
			}
//			if (employeeSearch.getEmpDetailsId() != null && employeeSearch.getEmpDetailsId().size() > 0) {
//				filters = new HashMap<String, Object>();
//				filters.put(Constants.IN, 0);
//				dynamicFilters.put(Constants.EMP_ID, filters);
//			}
			if (employeeSearch.getEmpId() != null) {
				filters = new HashMap<String, Object>();
				filters.put(Constants.EQUALS, employeeSearch.getEmpId());
				dynamicFilters.put(Constants.EMP_ID, filters);
			}
			if (employeeSearch.getEmailId() != null) {
				filters = new HashMap<String, Object>();
				filters.put(Constants.LIKE, employeeSearch.getEmailId());
				dynamicFilters.put(Constants.EMAIL_ID, filters);
			}
			result = new Result();
			if (!employeeSearch.isExport()) {

				Page<Employee> pageResult = employeeDAO.findAll(GenericSpecification.getSpecification(dynamicFilters),
						GenericSpecification.getPagination(employeeSearch));
				SearchResult<EmployeeDTO> searchResult = GenericSpecification.getPaginationDetails(pageResult,
						EmployeeDTO.class);
				List<EmployeeDTO> employeeDTOs = MAPPER.fromEmployeeModel(pageResult.getContent());
				searchResult.setContent(employeeDTOs);

				result.setData(searchResult);
				if (employeeDTOs.size() == 0) {
					result.setStatusCode(HttpStatus.NOT_FOUND.value());
					result.setErrorMessage("No Result Found");
				} else {
					result.setStatusCode(HttpStatus.OK.value());
					result.setSuccessMessage("Fetching all employee details based on the selected filters");
				}
			} else {
				result.setData(exportData(employeeSearch, dynamicFilters));
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}

	/**
	 * Export data.
	 *
	 * @param employeeSearch the employee search
	 * @param dynamicFilters the dynamic filters
	 * @return the search result
	 */
	private SearchResult<EmployeeDTO> exportData(EmployeeSearch employeeSearch,
			Map<String, Map<String, Object>> dynamicFilters) {
		SearchResult<EmployeeDTO> searchResult = new SearchResult<>();
		try {
			List<Employee> sortResult = employeeDAO.findAll(GenericSpecification.getSpecification(dynamicFilters),
					GenericSpecification.getSort(employeeSearch));

			List<EmployeeDTO> employeeDTOs = MAPPER.fromEmployeeModel(sortResult);

			searchResult.setContent(employeeDTOs);
		} catch (Exception e) {
			log.error("Error in exportData :: ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return searchResult;
	}

	@Override
	public Result findByListEmpId(EmployeeListSearch employeeListSearch) {
		Result result = new Result();
		try {
			List<EmployeeDTO> employees = MAPPER
					.fromEmployeeModel(employeeDAO.findByEmpIdIn(employeeListSearch.getEmpId()));
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
	public Result findByEmailIdAndFirstName(String emailId, String firstName) {
		Result result = new Result();
		try {
			EmployeeDTO employeeDTO = MAPPER
					.fromEmployeeModel(employeeDAO.findByEmailIdAndFirstName(emailId, firstName));
			if (employeeDTO == null) {
				result.setStatusCode(HttpStatus.NOT_FOUND.value());
				result.setErrorMessage("doesn't exist");
			} else {
				result.setData(employeeDTO);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Fetching success.");

			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
}
