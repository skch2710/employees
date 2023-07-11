package com.springboot.employees.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MapColumns {

	ID("id", "id"), SATHISH_KUMAR("sathishKumar", "sathish_kumar");

	private final String fieldName;
	private final String dbColumnName;

	public static String getDBColumnName(String key) {
		String name = "";
		for (MapColumns e : values()) {
			if (e.getFieldName().equalsIgnoreCase(key)) {
				name = e.getDbColumnName();
				break;
			}
		}
		return name;
	}

}
