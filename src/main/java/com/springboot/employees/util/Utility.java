package com.springboot.employees.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.employees.common.Constants;

@Component
public class Utility {

	public static String paddingFormat(String input) {
		if (input != null && !input.isEmpty()) {
			return String.format("%010d", Long.parseLong(input));
		}
		return "";
	}

	public static List<String> paddingFormat(List<String> input) {
		List<String> outputList = new ArrayList<>();
		if (input != null && !input.isEmpty()) {
			for (String str : input) {
				String output = String.format("%010d", Long.parseLong(str));
				outputList.add(output);
			}
		}
		return outputList;
	}

	public static String paddingFormat1(String input) {
		if (input != null && !input.isEmpty()) {
			if (input.length() < 10) {
				return "#".repeat(10 - input.length()) + input;
			}
			return input;
		}
		return "";
	}

	public static String nullChech(String input) {
		return input != null ? input : "";
	}

	public static String dateConvert(LocalDate date) {
		if (date != null) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			return dateTimeFormatter.format(date);
		} else {
			return "";
		}
	}

	public static String dateConvert(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			return sdf.format(date);
		} else {
			return "";
		}
	}

	public static LocalDate dateConvert(String date) {
		if (date != null && !date.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate localDate = LocalDate.parse(date, formatter);
			return localDate;
		} else {
			return null;
		}
	}

	public static BigDecimal numberConvert(String number) {
		if (number != null && !number.isEmpty()) {
			// Remove currency symbol and grouping separators
			String cleanedValue = number.replace("$", "").replace(",", "");
			return new BigDecimal(cleanedValue.trim());
		} else {
			return null;
		}
	}

	public static Double numberConvert(BigDecimal value) {
		if (value != null) {
			return value.doubleValue(); // Convert BigDecimal to double
		} else {
			return 0d;
		}
	}

	public static void createFileToZip(ZipOutputStream zipOutputStream, String fileName, ByteArrayOutputStream baos)
			throws IOException {
		// Add the byte array to the zip as an entry
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOutputStream.putNextEntry(zipEntry);
		zipOutputStream.write(baos.toByteArray());
		zipOutputStream.closeEntry();
	}

	public static Date convertDate(String date) throws ParseException {
		if (date != null && !date.isEmpty()) {
			return new SimpleDateFormat(Constants.DATE_FORMAT).parse(date);
		} else {
			return null;
		}
	}

	public static ByteArrayOutputStream readImageToByteArrayOutputStream(String imagePath) throws IOException {
		Path filePath = Paths.get(imagePath);
		byte[] imageBytes = Files.readAllBytes(filePath);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(imageBytes);

		return outputStream;
	}

	public ByteArrayOutputStream convertMultipartFileToByteArrayOutputStream(MultipartFile imageFile)
			throws IOException {
		byte[] imageBytes = imageFile.getBytes();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(imageBytes);

		return outputStream;
	}
}
