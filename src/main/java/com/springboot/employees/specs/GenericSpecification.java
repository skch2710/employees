package com.springboot.employees.specs;

import org.springframework.data.domain.Page;
import com.springboot.employees.dto.SearchResult;

public class GenericSpecification {

	/**
	 * Gets the pagination details.
	 *
	 * @param <T>        the generic type
	 * @param pageResult the page result
	 * @param c          the class
	 * @return the pagination details
	 */
	public static <T> SearchResult<T> getPaginationDetails(Page<?> pageResult, Class<T> c) {
		SearchResult<T> searchResult = new SearchResult<>();
		searchResult.setPageNo(pageResult.getNumber() + 1);
		searchResult.setPageSize(pageResult.getSize());
		searchResult.setTotalElements(pageResult.getTotalElements());
		searchResult.setTotalPages(pageResult.getTotalPages());
		searchResult.setLastPage(pageResult.isLast());
		return searchResult;
	}

}
