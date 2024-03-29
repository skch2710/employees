package com.springboot.employees.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Constants {
	public static final String SATHISH_KUMAR = "ch sathish kumar";
	public static final Long MONTHS = 12L;
	public static final String FIRST_NAME = "firstName";
	
	public static final String EQUALS = "=";
	public final static String EQUALS_NULL = "equal_null";
	public final static String NOT_EQUALS = "!=";
	public final static String NOT_EQUALS_NULL = "not_equal_null";
	public final static String LIKE = "contains";
	public final static String IN = "in";
	public final static String NOT_IN = "not_in";
	public final static String IN_NULL = "in_null";
	public final static String NOT_IN_NULL = "not_in_null";
	public final static String STRING_IN = "stringIn";
	public final static String STRING_NOT_IN = "stringNotIn";
	public final static String BETWEEN = "between";
	public final static String LESS_THAN = "<";
	public final static String GREATER_THAN = ">";
	public final static String LESS_THAN_OR_EQUAL = "<=";
	public final static String GREATER_THAN_OR_EQUAL = ">=";
	public final static String GREATER_THAN_EQUAL = "gte";
	public final static String GTE_NULL = "gte_null";
	public final static String LESS_THAN_EQUAL = "lte";
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public final static String TIME_STAMP_FORMAT = "MM/dd/yyyy HH:mm:ss.SSS";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String EMP_ID = "empId";
	public static final String EMAIL_ID = "emailId";
	
	public static final String BETWEEN_DATE = "betweenDate";
	public static final String NULL = "null";
	
	public static final String DOB = "dob";
	
	public static final List<Long> EMP_IDs = Arrays.asList(4L,8L);
	public static final ArrayList<Integer> NUMBERS = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
	
	public static final String IMAGE_LOGO  = "src/main/resources/images/image.png";
	public static final String STUDENT_ID = "studentId";
	public static final String FULL_NAME = "fullName";
	public static final String BETWEEN_TIME_STAMP = "betweenTimeStamp";
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
	public static final String SALARY = "salary";
	public static final String START_WITH = "startWith";
	
	public static final String ACCESS_DENIED = "You are not access to this resorce.";
	public static final String CURRENCY_FORMAT = "₹ #,##0.00";
	public static final String CURRENCY_FORMAT_NEGITIVE = "₹ #,##0.00; ₹ (#,##0.00)";
	public static final String NUMBER_FORMAT = "#,##0.00";
	public static final String NUMBER_FORMAT_NEGITIVE = "#,##0.00; (#,##0.00)";
	public static final String PERCENTAGE_FORMAT = "0.00\\%";
	public static final String PERCENTAGE_FORMAT_NEGITIVE = "0.00\\%; (0.00\\%)";
	
	public static final String ZIP_TYPE = "application/zip";
	public static final String EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String PDF_TYPE = "application/pdf";
	public static final String JPG_TYPE = "image/jpeg";
	public static final String PNG_TYPE = "image/png";
	
}
