package com.springboot.employees.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.springboot.employees.common.Constants;
import com.springboot.employees.dao.StudentDAO;
import com.springboot.employees.dto.Result;
import com.springboot.employees.dto.SearchResult;
import com.springboot.employees.dto.StudentDTO;
import com.springboot.employees.dto.StudentSearch;
import com.springboot.employees.email.EmailService;
import com.springboot.employees.exception.CustomException;
import com.springboot.employees.mapper.ObjectMapper;
import com.springboot.employees.model.Student;
import com.springboot.employees.service.StudentService;
import com.springboot.employees.specs.GenericSpecification;
import com.springboot.employees.util.PdfHelper;
import com.springboot.employees.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

	private ObjectMapper MAPPER = ObjectMapper.INSTANCE;

	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
	private EmailService emailService;

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

				Page<Student> pageResult = studentDAO.getStudents(studentSearch.getEmailId(),
						GenericSpecification.getSpecification(dynamicFilters),
						GenericSpecification.getPagination(studentSearch));

				SearchResult<Student> searchResult = GenericSpecification.getPaginationDetails(pageResult,
						Student.class);
				searchResult.setContent(pageResult.getContent());

				result.setData(searchResult);
				result.setStatusCode(HttpStatus.OK.value());
				result.setSuccessMessage("Getting Student Details.");
			} else {
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
	 * @param studentSearch  the student search
	 * @param dynamicFilters the dynamic filters
	 * @return the search result
	 */
	private SearchResult<Student> exportData(StudentSearch studentSearch,
			Map<String, Map<String, Object>> dynamicFilters) {
		SearchResult<Student> searchResult = new SearchResult<>();
		try {
			List<Student> sortResult = studentDAO.getStudents(studentSearch.getEmailId(),
					GenericSpecification.getSpecification(dynamicFilters), GenericSpecification.getSort(studentSearch));
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
			List<Student> students = batchInsertRecords(studentDTOs);
			
			if(!students.isEmpty()) {
				
				ByteArrayOutputStream excelStream = downloadStudentExcel();
				ByteArrayOutputStream pdfStream = generatePdfWithTable();
				
				students.forEach(student->{
					Map<String, Object> model = new HashMap<>();
					model.put("fullName", student.getFullName());
					emailService.sendEmailWelcome(model, student.getEmailId(),excelStream,pdfStream);
				});
			}

			result = new Result(students);
			result.setStatusCode(HttpStatus.OK.value());
			result.setSuccessMessage("Students Saved Successfully.");

		} catch (Exception e) {
			log.error("Error in Saving Students :: ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	
	private List<Student> batchInsertRecords(List<StudentDTO> records) {
		List<Student> studentsList = new ArrayList<>();
		int batchSize = 20; // Set the desired batch size
		for (int i = 0; i < records.size(); i += batchSize) {
			int endIndex = Math.min(i + batchSize, records.size());
			List<StudentDTO> batch = records.subList(i, endIndex);
			studentsList.addAll(studentDAO.saveAll(MAPPER.fromStudentDTO(batch)));
		}
		return studentsList;
	}

	/**
	 * Download Excel File Download
	 * 
	 */

	@Override
	public ByteArrayOutputStream downloadStudentExcel() {
		SXSSFWorkbook workbook;
		ByteArrayOutputStream baos;
		try {
			workbook = new SXSSFWorkbook();
			SXSSFSheet sheet = workbook.createSheet("Data");

			// Simulate retrieving all records
			List<Student> records = studentDAO.findAll();
//			List<StudentDTO> downloadStudents = MAPPER.fromStudent(records);

			// Define cell styles for header
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font headerFont = workbook.createFont();
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerCellStyle.setFont(headerFont);

			CellStyle currencyCellStyle = workbook.createCellStyle();
			DataFormat currencyDataFormat = workbook.createDataFormat();
			currencyCellStyle.setDataFormat(currencyDataFormat.getFormat("$ #,##0.00"));

			// Write the records to the Excel sheet
			String[] HEADERs = { "Full Name", "Email ID", "DOB", "Mobile Number", "Salary", "From Date", "To Date" };

			Row row = sheet.createRow(0);

			int cellId = 0;
			for (String string : HEADERs) {
				Cell cell = row.createCell(cellId++);
				cell.setCellValue(string);
				cell.setCellStyle(headerCellStyle); // Apply the header cell style
			}

			sheet.createFreezePane(0, 1); // Freezing Headers

			int rowid = 1;
			for (Student studentDTO : records) {
				row = sheet.createRow(rowid++);
				row.createCell(0).setCellValue(Utility.nullChech(studentDTO.getFullName()));
				row.createCell(1).setCellValue(Utility.nullChech(studentDTO.getEmailId()));
				row.createCell(2).setCellValue(Utility.dateConvert(studentDTO.getDob()));
				row.createCell(3).setCellValue(Utility.nullChech(studentDTO.getMobileNumber()));
				Cell cell4 = row.createCell(4);
				cell4.setCellStyle(currencyCellStyle);
				cell4.setCellValue(Utility.numberConvert(studentDTO.getSalary()));
				row.createCell(5).setCellValue(Utility.dateConvert(studentDTO.getFromDate()));
				row.createCell(6).setCellValue(Utility.dateConvert(studentDTO.getToDate()));
			}

			sheet.setColumnWidth(0, 14 * 256);
			sheet.setColumnWidth(1, 24 * 256);
			sheet.setColumnWidth(2, 14 * 256);
			sheet.setColumnWidth(3, 16 * 256);
			sheet.setColumnWidth(4, 14 * 256);
			sheet.setColumnWidth(5, 14 * 256);
			sheet.setColumnWidth(6, 14 * 256);

			baos = new ByteArrayOutputStream();
			workbook.write(baos); // Write the workbook to a temporary byte array
			workbook.close(); // Close the workbook
		} catch (Exception e) {
			log.error("Error in create workbook :: ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return baos;
	}
	
	@Override
	public ByteArrayOutputStream generatePdfWithTable() {
		ByteArrayOutputStream baos;
		try {
//			Rectangle pagesize = new Rectangle(1754, 1240);

			// Create a new document
			Document document = new Document(PageSize.A4, 30, 30, 45, 30);

			baos = new ByteArrayOutputStream();

			// Create a new PDF writer
			PdfWriter.getInstance(document, baos);

			// Open the document
			document.open();

			// Add the image to the Document
			document.add(PdfHelper.createImage(Constants.IMAGE_LOGO));

			PdfPTable billToAndSummaryTable = PdfHelper.createNoBorderTable(2, 5, 5, 100);

			PdfPTable billTo = PdfHelper.createTable(1, 5, 5, 100);
			PdfHelper.noBorderCell(billTo, "Bill To", PdfHelper.getPoppinsFont(12, null), 3, Element.ALIGN_LEFT);
			PdfHelper.noBorderCell(billTo, "Cover Entity Name - HRSA ID", PdfHelper.getPoppinsFont(12, null), 3,
					Element.ALIGN_LEFT);
			PdfHelper.noBorderCell(billTo, "Address Line 1", PdfHelper.getPoppinsFont(12, null), 3, Element.ALIGN_LEFT);
			PdfHelper.noBorderCell(billTo, "Address Line 2", PdfHelper.getPoppinsFont(12, null), 3, Element.ALIGN_LEFT);
			PdfHelper.noBorderCell(billTo, "City , State Zip", PdfHelper.getPoppinsFont(12, null), 3,
					Element.ALIGN_LEFT);

			billToAndSummaryTable.addCell(billTo);

			PdfPTable summaryTable = PdfHelper.createTable(1, 5, 5, 100);
			PdfHelper.createPdfPCell(summaryTable, "Summary Table", PdfHelper.getPoppinsFont(12, null), 3, 50,
					Element.ALIGN_LEFT);
			PdfHelper.createPdfPCell(summaryTable, "Cover Entity Name - HRSA ID", PdfHelper.getPoppinsFont(12, null), 3,
					50, Element.ALIGN_LEFT);
			PdfHelper.createPdfPCell(summaryTable, "Address Line 1", PdfHelper.getPoppinsFont(12, null), 3, 50,
					Element.ALIGN_LEFT);
			PdfHelper.createPdfPCell(summaryTable, "Address Line 2", PdfHelper.getPoppinsFont(12, null), 3, 50,
					Element.ALIGN_LEFT);
			PdfHelper.createPdfPCell(summaryTable, "City , State Zip", PdfHelper.getPoppinsFont(12, null), 3, 50,
					Element.ALIGN_LEFT);

			billToAndSummaryTable.addCell(summaryTable);

			document.add(billToAndSummaryTable);

			createLineSeparator(document);

			// Student Table Data
			List<Student> students = studentDAO.findAll();
			createStudentTable(students, document);

			PdfPTable belowTable = PdfHelper.createNoBorderTable(1, 5, 5, 80);
			PdfHelper.noBorderCell(belowTable, "Value   -   $50 ", PdfHelper.getPoppinsFont(12, null), 15,
					Element.ALIGN_RIGHT);

			document.add(belowTable);

			// Close the document
			document.close();

		} catch (Exception e) {
			log.error("error in generate Pdf ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return baos;
	}

	private void createLineSeparator(Document document) throws DocumentException {
		LineSeparator ls = new LineSeparator();
		ls.setOffset(5);

		PdfPTable lineTable = PdfHelper.createNoBorderTable(3, 5, 5, 80);
		lineTable.setTotalWidth(new float[] { 40, 10, 40 });
		PdfHelper.createPdfPCell(lineTable, ls);
		PdfHelper.noBorderCell(lineTable, "LINE", PdfHelper.getPoppinsFont(12, null), 5, Element.ALIGN_CENTER);
		PdfHelper.createPdfPCell(lineTable, ls);

		document.add(lineTable);
	}

	private void createStudentTable(List<Student> students, Document document) {
		try {
			// Table count Header
			if (students.size() != 0) {
				Paragraph studentTableTitle = PdfHelper.createParagraph("Number of Students (" + students.size() + ")",
						14, 5, PdfHelper.getPoppinsFont(12, new BaseColor(255, 165, 0)));
				document.add(studentTableTitle);
			} else {
				Paragraph studentTableTitle = PdfHelper.createParagraph("Number of Students (0)", 12, 5,
						PdfHelper.getPoppinsFont(14, new BaseColor(255, 165, 0)));
				document.add(studentTableTitle);
			}

			// Create a table with 6 columns
			PdfPTable studentTable = PdfHelper.createTable(6, 5, 5, 100);
			studentTable.setTotalWidth(new float[] { 15, 16, 22, 16, 16, 15 });

			// Add table headers
			List<String> studentHeaders = Arrays.asList("Student ID", "Full Name", "Email ID", "Mobile Number", "DOB",
					"Salary");

			studentHeaders.forEach(studentHeader -> {
				PdfHelper.headerCell(studentTable, studentHeader, new BaseColor(229, 242, 255),
						PdfHelper.getPoppinsFont(12, null));
			});

			BigDecimal totalSalary = new BigDecimal(0);

			// Add table rows with data
			if (!students.isEmpty()) {
				for (Student student : students) {

					if (student.getSalary() != null) {
						totalSalary = totalSalary.add(student.getSalary());
					}

					PdfHelper.createPdfPCell(studentTable, PdfHelper.numberConvert(student.getStudentId()),
							PdfHelper.getPoppinsFont(11, null), 5, Element.ALIGN_CENTER);
					PdfHelper.createPdfPCell(studentTable, student.getFullName(), PdfHelper.getPoppinsFont(11, null), 5,
							Element.ALIGN_CENTER);
					PdfHelper.createPdfPCell(studentTable, student.getEmailId(), PdfHelper.getPoppinsFont(11, null), 5,
							Element.ALIGN_CENTER);
					PdfHelper.createPdfPCell(studentTable, student.getMobileNumber(),
							PdfHelper.getPoppinsFont(11, null), 5, Element.ALIGN_CENTER);
					PdfHelper.createPdfPCell(studentTable, PdfHelper.dateConvert(student.getDob()),
							PdfHelper.getPoppinsFont(11, null), 5, Element.ALIGN_CENTER);
					PdfHelper.createPdfPCell(studentTable, PdfHelper.numberFormatGrid(student.getSalary()),
							PdfHelper.getPoppinsFont(11, null), 5, Element.ALIGN_RIGHT);

				}
			}

			// Create a table of total
			PdfHelper.noBorderCell(studentTable, "Total", PdfHelper.getPoppinsBoldFont(12, null), 5, Element.ALIGN_LEFT,
					5, 20);
			PdfHelper.noBorderCell(studentTable, PdfHelper.numberFormatGrid(totalSalary),
					PdfHelper.getPoppinsBoldFont(12, null), 5, Element.ALIGN_RIGHT);

			// Add the table to the document
			document.add(studentTable);

		} catch (Exception e) {
			log.error("error in create Student Table ", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
