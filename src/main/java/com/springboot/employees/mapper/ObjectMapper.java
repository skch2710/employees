package com.springboot.employees.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.springboot.employees.dto.EmployeeDTO;
import com.springboot.employees.dto.EmployeeDetailsDTO;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.model.Employee;
import com.springboot.employees.model.EmployeeDetails;
import com.springboot.employees.model.Student;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ObjectMapper {

	ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);
	
	@Mapping(source = "employeeDetails.salary", target = "salary",numberFormat = "₹#,##0.00")
	@Mapping(source = "employeeDetails.address", target = "address")
	EmployeeDTO fromEmployeeModel(Employee employee);
	
	List<EmployeeDTO> fromEmployeeModel(List<Employee> employee);
	
	@Mapping(source = "employee.empId", target = "empId")
	@Mapping(source = "employee.firstName", target = "firstName")
	@Mapping(source = "employee.lastName", target = "lastName")
	@Mapping(source = "employee.emailId", target = "emailId")
	EmployeeDetailsDTO fromEmployeeDetailsModel(EmployeeDetails employeeDetail);
	
	List<EmployeeDetailsDTO> fromEmployeeDetailsModel(List<EmployeeDetails> employeeDetails);
	
//	@InheritInverseConfiguration
//	Employee fromEmployeeDTO(EmployeeDTO employeeDTO);
	
	List<Student> fromStudentDTO(List<StudentDTO> studentDTOs);
}
