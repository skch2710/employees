package com.springboot.employees.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.springboot.employees.model.Student;

@Component
public class StudentBatch {

	private final JdbcTemplate jdbcTemplate;

	public StudentBatch(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private String insertQuery = "INSERT INTO public.students( dob, email_id, from_date, full_name, mobile_number, salary, to_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

	@Transactional
	public void saveInBatch(List<Student> studentList) {

		jdbcTemplate.batchUpdate(this.insertQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException, DataAccessException {
				Student model = studentList.get(i);
				ps.setObject(1, model.getDob(), Types.DATE);
				ps.setObject(2, model.getEmailId(), Types.VARCHAR);
				ps.setObject(3, model.getFromDate(), Types.DATE);
				ps.setObject(4, model.getFullName(), Types.VARCHAR);
				ps.setObject(5, model.getMobileNumber(), Types.VARCHAR);
				ps.setObject(6, model.getSalary(), Types.NUMERIC);
				ps.setObject(7, model.getToDate(), Types.DATE);
			}

			@Override
			public int getBatchSize() {
				return studentList.size();
			}
		});
	}
}