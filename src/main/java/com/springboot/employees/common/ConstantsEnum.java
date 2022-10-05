package com.springboot.employees.common;

import lombok.Getter;

public enum ConstantsEnum {
	
	SATHISH("sathish"),
	KUMAR("kumar");
	
	@Getter
	private String value;

	ConstantsEnum(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}
