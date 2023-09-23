package com.springboot.employees.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestHelper {

//	String URL = "SERVICE_NAME"+"API_NAME"; //POST
//	String URL = "SERVICE_NAME"+"API_NAME"+"/"+id; //GET
//	ResultRest result = restGet(URL, accessToken, ResultRest.class);
//	DTO dtoList = result.getData();

	public static <T> T restPost(String url, Object requestObject, String accessToken, Class<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<?> request = new HttpEntity<>(requestObject, headers);

		ResponseEntity<T> response = new RestTemplate().exchange(url, HttpMethod.POST, request, responseType);

		return response.getBody();
	}

	public static <T> T restGet(String url, String accessToken, Class<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<T> response = new RestTemplate().exchange(url, HttpMethod.GET, request, responseType);

		return response.getBody();
	}

}
