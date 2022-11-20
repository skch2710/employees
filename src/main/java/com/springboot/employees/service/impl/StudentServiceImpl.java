package com.springboot.employees.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.employees.common.Constants;
import com.springboot.employees.dao.StudentDAO;
import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeSearch;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.SearchResult;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentSearch;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.mapper.ObjectMapper;
import com.springboot.employees.model.Employee;
import com.springboot.employees.model.Student;
import com.springboot.employees.service.StudentService;
import com.springboot.employees.specs.GenericSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

	private ObjectMapper MAPPER = ObjectMapper.INSTANCE;

	@Autowired
	private StudentDAO studentDAO;

	/**
	 * Get Student Details By EmailId By Pagination And Sort of Native Query
	 * 
	 * @param StudentSearch studentSearch
	 * @return Result result
	 * 
	 */
	@Override
	public Result getStudents(StudentSearch studentSearch) {
		Long start = System.currentTimeMillis();
		Result result = null;
		try {
			Map<String, Object> filters = null;
			Map<String, Map<String, Object>> dynamicFilters = new HashMap<>();

			if (studentSearch.getSortBy() == null || "".equalsIgnoreCase(studentSearch.getSortBy())) {
				studentSearch.setSortBy(Constants.FIRST_NAME);
			}
			
			if (studentSearch.getColumnFilters() != null) {
				GenericSpecification.dynamicFilters(studentSearch.getColumnFilters(), dynamicFilters);
			}
//			Pageable paging = PageRequest.of(studentSearch.getPageNumber(), studentSearch.getPageSize());
			
			result = new Result();
			
			if (!studentSearch.isExport()) {
				
				Page<Student> pageResult = studentDAO.getStudents(studentSearch.getEmailId(),GenericSpecification.getSpecification(dynamicFilters),GenericSpecification.getPagination(studentSearch));
				
				SearchResult<Student> searchResult = GenericSpecification.getPaginationDetails(pageResult,
						Student.class);
				searchResult.setContent(pageResult.getContent());
				
				result.setData(searchResult);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Getting Student Details.");
			}else {
				result.setData(exportData(studentSearch, dynamicFilters));
			}

		} catch (Exception e) {
			log.error("Error in get Students Details :: {}", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Long end = System.currentTimeMillis();
		log.info(">>>>>>>>>>>Time Calucated :: {}", (end - start));
		return result;
	}
	
	/**
	 * Export data.
	 *
	 * @param studentSearch the student search
	 * @param dynamicFilters the dynamic filters
	 * @return the search result
	 */
	private SearchResult<Student> exportData(StudentSearch studentSearch,
			Map<String, Map<String, Object>> dynamicFilters) {
		SearchResult<Student> searchResult = new SearchResult<>();
		try {
			List<Student> sortResult = studentDAO.getStudents(studentSearch.getEmailId(),GenericSpecification.getSpecification(dynamicFilters),
					GenericSpecification.getSort(studentSearch));
			searchResult.setContent(sortResult);
		} catch (Exception e) {
			log.error("Error in exportData :: ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return searchResult;
	}
	
	
	
	/**
	 * Saving Students
	 * 
	 * @param List studentDTO
	 * @return Result the result
	 */
	@Override
	public Result saveStudent(List<StudentDTO> studentDTOs) {

		Result result = null;

		try {
			List<Student> students = studentDAO.saveAll(MAPPER.fromStudentDTO(studentDTOs));

			result = new Result(students);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("Students Saved Successfully.");

		} catch (Exception e) {
			log.error("Error in Saving Students :: ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}

}
