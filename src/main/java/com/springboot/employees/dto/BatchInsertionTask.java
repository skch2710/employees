package com.springboot.employees.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springboot.employees.dao.StudentDAO;
import com.springboot.employees.model.Student;

public class BatchInsertionTask implements Runnable {

	private List<Student> records;
	private StudentDAO studentDAO;
//	private StudentBatch studentBatch;

	public BatchInsertionTask(List<Student> records, StudentDAO studentDAO) {
		this.records = new CopyOnWriteArrayList<>(records);
		this.studentDAO = studentDAO;
	}
//	public BatchInsertionTask(List<Student> records, StudentBatch studentBatch) {
//		this.records = new CopyOnWriteArrayList<>(records);
//		this.studentBatch = studentBatch;
//	}

	@Override
	public void run() {
		studentDAO.saveAll(records);
	}

//	@Override
//	public void run() {
//		studentBatch.saveInBatch(records);
//	}

}