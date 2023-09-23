package com.springboot.employees.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelHelper {

	public static int getNumberOfHeaderCells(Sheet sheet) {
		Row headerRow = sheet.getRow(0);
		return headerRow.getPhysicalNumberOfCells();
	}

	public static List<String> getHeaders(Sheet sheet, int cellCount) {
		List<String> headers = new ArrayList<>();
		Row headerRow = sheet.getRow(0);
		for (int i = 0; i < cellCount; i++) {
			Cell cell = headerRow.getCell(i);
			headers.add(cell.getStringCellValue());
		}
		return headers;
	}

	public static Boolean headersCompare(Sheet sheet, List<String> headrs) {
		Row headerRow = sheet.getRow(0);
		if (headerRow.getPhysicalNumberOfCells() != headrs.size()) {
			return false;
		}
//		List<String> sheetHeaders = new ArrayList<>();
		for (int i = 0; i < headrs.size(); i++) {
			Cell cell = headerRow.getCell(i);
//			sheetHeaders.add(cell.getStringCellValue());
			if (!headrs.get(i).equals(cell.getStringCellValue())) {
				return false;
			}
		}
		return true;
	}
}
