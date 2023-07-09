package com.springboot.employees.specs;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.springboot.employees.common.Constants;
import com.springboot.employees.dto.ColumnFilter;
import com.springboot.employees.dto.Pagination;
import com.springboot.employees.dto.SearchResult;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class GenericSpecification.
 */
@Component

/** The Constant log. */
@Slf4j
public class GenericSpecification {

	/**
	 * Gets the specification.
	 *
	 * @param <T>            the generic type
	 * @param dynamicFilters the dynamic filters
	 * @return the specification
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static <T> Specification<T> getSpecification(Map<String, Map<String, Object>> dynamicFilters)
			throws ParseException {
		Specification<T> spec = null;
		Specification<T> tempSpec = null;
		for (Map.Entry<String, Map<String, Object>> entry : dynamicFilters.entrySet()) {
			for (Map.Entry<String, Object> entryFilter : entry.getValue().entrySet()) {

				switch (entryFilter.getKey()) {
				case "=":
					tempSpec = findByEquals(entry.getKey(), entryFilter.getValue());
					break;
				case "contains":
					tempSpec = findByContains(entry.getKey(), entryFilter.getValue());
					break;
				case "in":
					tempSpec = findByIn(entry.getKey(), (List<Long>) entryFilter.getValue());
					break;
				case "stringIn":
					tempSpec = findByStringIn(entry.getKey(), (List<String>) entryFilter.getValue());
					break;
				case "between":
					Map<String, String> dateMap = (Map<String, String>) entryFilter.getValue();
					tempSpec = findByInBetween(entry.getKey(), dateMap.get(Constants.START_DATE),
							dateMap.get(Constants.END_DATE));
					break;
				case "betweenTimeStamp":
		        	  Map<String, String> timeStampMap = (Map<String, String>) entryFilter.getValue();
		              tempSpec = findByInBetweenTimeStamp(entry.getKey(),timeStampMap.get(Constants.START_DATE), timeStampMap.get(Constants.END_DATE));
		              break;
				case "!=":
					tempSpec = findByNotEquals(entry.getKey(), entryFilter.getValue());
//					 return criteriaBuilder.notEqual(root.get(criteria.getKey()),
//					 criteria.getValue());
				case "<":
					tempSpec = findByLessThan(entry.getKey(), entryFilter.getValue());
					break;
				case ">":
					tempSpec = findByGreaterThan(entry.getKey(), entryFilter.getValue());
					break;
				case "<=":
					tempSpec = findByLessThanOrEqual(entry.getKey(), entryFilter.getValue());
					break;
				case ">=":
					tempSpec = findByGreaterThanOrEqual(entry.getKey(), entryFilter.getValue());
					break;
				case "gte":
		              tempSpec = findByGreaterThanOrEqualObject(entry.getKey(),entryFilter.getValue());
		              break;
				case "lte":
		              tempSpec = findByLessThanOrEqualObject(entry.getKey(),entryFilter.getValue());
		              break;
				case "startWith":
					tempSpec = findByStartWith(entry.getKey(), entryFilter.getValue());
					break;
				case "betweenDate":
					SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
					Date date = formatter.parse(entry.getValue().get("betweenDate").toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					Date startDate = cal.getTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.set(Calendar.MILLISECOND, 999);
					Date endDate = calendar.getTime();
					tempSpec = findDateByInBetween(entry.getKey(), startDate, endDate);
					break;
				}
				spec = spec != null ? Specification.where(spec).and(tempSpec) : tempSpec;
				// System.out.println(spec.toString() + " ***********" + ++count);
			}
		}
		return spec;
	}

	/**
	 * Additional specification.
	 *
	 * @param <T>            the generic type
	 * @param spec           the spec
	 * @param dynamicFilters the dynamic filters
	 * @return the specification
	 * @throws ParseException the parse exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> Specification<T> additionalSpecification(Specification<T> spec,
			Map<String, Map<String, Object>> dynamicFilters) throws ParseException {
		Specification<T> tempSpec = null;
		for (Map.Entry<String, Map<String, Object>> entry : dynamicFilters.entrySet()) {
			for (Map.Entry<String, Object> entryFilter : entry.getValue().entrySet()) {

				switch (entryFilter.getKey()) {
				case "=":
					tempSpec = findByEquals(entry.getKey(), entryFilter.getValue());
					break;
				case "contains":
					tempSpec = findByContains(entry.getKey(), entryFilter.getValue());
					break;
				case "in":
					tempSpec = findByIn(entry.getKey(), (List<Long>) entryFilter.getValue());
					break;
				case "stringIn":
					tempSpec = findByStringIn(entry.getKey(), (List<String>) entryFilter.getValue());
					break;
				case "between":
					Map<String, String> dateMap = (Map<String, String>) entryFilter.getValue();
					tempSpec = findByInBetween(entry.getKey(), dateMap.get(Constants.START_DATE),
							dateMap.get(Constants.END_DATE));
					break;
				case "betweenTimeStamp":
		        	  Map<String, String> timeStampMap = (Map<String, String>) entryFilter.getValue();
		              tempSpec = findByInBetweenTimeStamp(entry.getKey(),timeStampMap.get(Constants.START_DATE), timeStampMap.get(Constants.END_DATE));
		              break;
				case "!=":
					tempSpec = findByNotEquals(entry.getKey(), entryFilter.getValue());
//					 return criteriaBuilder.notEqual(root.get(criteria.getKey()),
//					 criteria.getValue());
				case "<":
					tempSpec = findByLessThan(entry.getKey(), entryFilter.getValue());
					break;
				case ">":
					tempSpec = findByGreaterThan(entry.getKey(), entryFilter.getValue());
					break;
				case "<=":
					tempSpec = findByLessThanOrEqual(entry.getKey(), entryFilter.getValue());
					break;
				case ">=":
					tempSpec = findByGreaterThanOrEqual(entry.getKey(), entryFilter.getValue());
					break;
				case "gte":
		              tempSpec = findByGreaterThanOrEqualObject(entry.getKey(),entryFilter.getValue());
		              break;
				case "lte":
		              tempSpec = findByLessThanOrEqualObject(entry.getKey(),entryFilter.getValue());
		              break;
				case "startWith":
					tempSpec = findByStartWith(entry.getKey(), entryFilter.getValue());
					break;
				case "betweenDate":
					SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
					Date date = formatter.parse(entry.getValue().get("betweenDate").toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					Date startDate = cal.getTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.set(Calendar.MILLISECOND, 999);
					Date endDate = calendar.getTime();
					tempSpec = findDateByInBetween(entry.getKey(), startDate, endDate);
					break;
				}
				spec = spec != null ? Specification.where(spec).and(tempSpec) : tempSpec;
				// System.out.println(spec.toString() + " ***********" + ++count);
			}
		}
		return spec;
	}

	/**
	 * Find by in between.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @return the specification
	 */
	private static <T> Specification<T> findByInBetween(String columnName, String startDate, String endDate) {
		return (rt, qry, cb) -> {
			return cb.between(rt.get(columnName), convertToDate(startDate), convertToDate(endDate));
		};
	}

	 /**
	   * Find by in between time stamp.
	   *
	   * @param <T> the generic type
	   * @param columnName the column name
	   * @param startDate the start date
	   * @param endDate the end date
	   * @return the specification
	   */
	  private static <T> Specification<T> findByInBetweenTimeStamp(String columnName, String startDate,
		      String endDate) {
		    return (rt, qry, cb) -> {
		      return cb.between(rt.get(columnName), convertToTimeStamp(startDate), convertToTimeStamp(endDate));
		    };
		  }
	  
	  /**
	   * Convert to time stamp.
	   *
	   * @param value the value
	   * @return the date
	   */
	  private static Date convertToTimeStamp(Object value) {
		    try {
		      return new SimpleDateFormat(Constants.TIME_STAMP_FORMAT).parse(value.toString());
		    } catch (Exception e) {
		    }
		    return null;
		  }
	  
	  private static LocalDate convertToTimeStampToLocalDate(Object value) {
		    try {
		      return LocalDate.parse( value.toString(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) );
		    } catch (Exception e) {
		    }
		    return null;
		  }
	  
	
	/**
	 * Find by less than.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param date       the date
	 * @return the specification
	 */
	private static <T> Specification<T> findByLessThan(String columnName, Object date) {
		return (rt, qry, cb) -> {
			return cb.lessThan(rt.get(columnName), convertToDate(date));
		};
	}

	/**
	 * Find by greater than.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param date       the date
	 * @return the specification
	 */
	private static <T> Specification<T> findByGreaterThan(String columnName, Object date) {
		return (rt, qry, cb) -> {
			return cb.greaterThan(rt.get(columnName), convertToDate(date));
		};
	}

	/**
	 * Find by less than or equal.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param date       the date
	 * @return the specification
	 */
	private static <T> Specification<T> findByLessThanOrEqual(String columnName, Object date) {
		return (rt, qry, cb) -> {
			return cb.lessThanOrEqualTo(rt.get(columnName), convertToDate(date));
		};
	}

	/**
	 * Find by greater than or equal.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param date       the date
	 * @return the specification
	 */
	private static <T> Specification<T> findByGreaterThanOrEqual(String columnName, Object date) {
		return (rt, qry, cb) -> {
			return cb.greaterThanOrEqualTo(rt.get(columnName), convertToDate(date));
		};
	}

	/**
	 * Find by greater than or equal object.
	 *
	 * @param <T> the generic type
	 * @param columnName the column name
	 * @param value the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByGreaterThanOrEqualObject(String columnName, Object value) {
		return (rt, qry, cb) -> {
			return cb.greaterThanOrEqualTo(rt.get(columnName), value.toString());
		};
	}
	
	private static <T> Specification<T> findByLessThanOrEqualObject(String columnName, Object value) {
		return (rt, qry, cb) -> {
			return cb.lessThanOrEqualTo(rt.get(columnName), value.toString());
		};
	}
	
	private static <T> Specification<T> findDateByInBetween(String columnName, Date startDate, Date endDate) {
		return (rt, qry, cb) -> {
			return cb.between(rt.get(columnName), convertToDateandTime(startDate), convertToDateandTime(endDate));
		};
	}

	/**
	 * Convert to date.
	 *
	 * @param value the value
	 * @return the date
	 */
	private static Date convertToDate(Object value) {
		try {
			return new SimpleDateFormat(Constants.DATE_FORMAT).parse(value.toString());
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Find by contains.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param value      the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByContains(String columnName, Object value) {
		return (rt, qry, cb) -> {
			return cb.like(cb.lower(rt.get(columnName)), "%" + value.toString().toLowerCase() + "%");
		};
	}

	/**
	 * Find by Start With.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param value      the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByStartWith(String columnName, Object value) {
		return (rt, qry, cb) -> {
			return cb.like(cb.lower(rt.get(columnName).as(String.class)), value.toString().toLowerCase() + "%");
		};
	}

	/**
	 * Find by equals.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param value      the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByEquals(String columnName, Object value) {
		return (rt, qry, cb) -> {
//      System.out.println(castToRequiredType(rt.get(columnName).getJavaType(), value));
			return cb.equal(rt.get(columnName), castToRequiredType(rt.get(columnName).getJavaType(), value));
		};
	}

	private static <T> Specification<T> findByNotEquals(String columnName, Object value) {
		return (rt, qry, cb) -> {
//      System.out.println(castToRequiredType(rt.get(columnName).getJavaType(), value));
			return cb.equal(rt.get(columnName), castToRequiredType(rt.get(columnName).getJavaType(), value));
		};
	}
	
	/**
	 * Find by in.
	 *
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param value      the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByIn(String columnName, List<Long> value) {
		return (rt, qry, cb) -> {
			return cb.in(rt.get(columnName)).value(castToRequiredType1(rt.get(columnName).getJavaType(), value));
		};
	}

	/**
	 * 
	 * Find by string in.
	 * 
	 * @param <T>        the generic type
	 * @param columnName the column name
	 * @param value      the value
	 * @return the specification
	 */
	private static <T> Specification<T> findByStringIn(String columnName, List<String> value) {
		return (rt, qry, cb) -> {
			return cb.in(rt.get(columnName)).value(castToRequiredType2(rt.get(columnName).getJavaType(), value));
		};
	}

	/**
	 * Cast to required type 2.
	 *
	 * @param fieldType the field type
	 * @param value     the value
	 * @return the object
	 */
	private static Object castToRequiredType2(Class fieldType, List<String> value) {
		List<Object> lists = new ArrayList<>();
		for (String s : value) {
			lists.add(s);
		}
		return lists;
	}

	private static Timestamp convertToDateandTime(Date value) {
		Timestamp ts = null;
		try {
			ts = new Timestamp(value.getTime());

		} catch (Exception e) {
			log.error("Error in findDateByInBetween :: ", e);
		}
		return ts;
	}

	/**
	 * Cast to required type.
	 *
	 * @param fieldType the field type
	 * @param value     the value
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	private static Object castToRequiredType(@SuppressWarnings("rawtypes") Class fieldType, Object value) {
		try {
			if (fieldType.isAssignableFrom(Double.class)) {
				return (Double) value;
			} else if (fieldType.isAssignableFrom(Long.class)) {
				return (Long) value;
			} else if (fieldType.isAssignableFrom(Integer.class)) {
				return (Integer) value;
			} else if (fieldType.isAssignableFrom(Boolean.class)) {
				return Boolean.valueOf(value.toString());
			} else if (fieldType.isAssignableFrom(Date.class)) {
				return new SimpleDateFormat(Constants.DATE_FORMAT).parse(value.toString());
			}else if (fieldType.isAssignableFrom(LocalDate.class)) {
				return LocalDate.parse( value.toString(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) );
			}
		} catch (Exception e) {
			log.error("Error in castToRequiredType :: ", e);
		}
		return value;
	}

	/**
	 * Cast to required type 1.
	 *
	 * @param fieldType the field type
	 * @param value     the value
	 * @return the object
	 */
	private static Object castToRequiredType1(Class fieldType, List<Long> value) {
		List<Object> lists = new ArrayList<>();
		for (Long s : value) {
			lists.add(s);
		}
		return lists;
	}

	/**
	 * Dynamic filters will be added in map based on the column filter in the table
	 * by clicking the header of the column.
	 *
	 * @param columnFilters  the column filters
	 * @param dynamicFilters the dynamic filters
	 */
	public static void dynamicFilters(ColumnFilter[] columnFilters, Map<String, Map<String, Object>> dynamicFilters) {
		log.debug("inside dynamicFilters");
		try {
			Map<String, Object> filters = null;
			for (ColumnFilter filter : columnFilters) {
				filters = new HashMap<String, Object>();
				if (filter.getColumn().getField().equalsIgnoreCase("dob")) {
					filters = new HashMap<String, Object>();
					filters.put(Constants.BETWEEN_DATE, filter.getValue());
					dynamicFilters.put(Constants.DOB, filters);
				} else if (filter.getColumn().getField().equalsIgnoreCase("startDate")) {
					filters = new HashMap<String, Object>();
					filters.put(Constants.BETWEEN_DATE, filter.getValue());
					dynamicFilters.put(Constants.START_DATE, filters);
				} else {
					filters.put(filter.getOperator(), filter.getValue());
					dynamicFilters.put(filter.getColumn().getField(), filters);
				}

			}
		} catch (Exception e) {
			log.error("Error in dynamicFilters :: ", e);
		}
	}

	/**
	 * Gets the sort.
	 *
	 * @param pagination the pagination
	 * @return the sort
	 */
	public static Sort getSort(Pagination pagination) {
		String[] sortBy = pagination.getSortBy().split(",");
		String sortOrder = pagination.getSortOrder();
		Sort sortGroup = null;
		for (String sField : sortBy) {
			if (sortOrder == null || sortOrder.equalsIgnoreCase("") || sortOrder.equalsIgnoreCase("asc")
					|| sortOrder.equalsIgnoreCase("0")) {
				Sort sort = Sort.by(Sort.Order.asc(sField.trim()).ignoreCase());
				sortGroup = sortGroup != null ? sortGroup.and(sort) : sort;
			} else {
				Sort sort = Sort.by(Sort.Order.desc(sField.trim()).ignoreCase());
				sortGroup = sortGroup != null ? sortGroup.and(sort) : sort;
			}
		}
		return sortGroup;
	}

	/**
	 * Gets the pagination.
	 *
	 * @param pagination the pagination
	 * @return the pagination
	 */
	public static Pageable getPagination(Pagination pagination) {
		int pageNumber = pagination.getPageNumber();
		int pageSize = pagination.getPageSize();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, getSort(pagination));
		return pageable;
	}

	/**
	 * Gets the pagination details.
	 *
	 * @param <T>        the generic type
	 * @param pageResult the page result
	 * @param c          the c
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
