package com.springboot.employees.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Utitlity {

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

}
