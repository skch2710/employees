package com.springboot.employees.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
	
	/**
	 * This method is used to call Token service
	 * @param Map<String,String> values
	 * @param Class<T> responseType
	 * @return <T> T
	 * 
	 * 	Map<String,String> values = new HashMap<>();
		values.put("url", "http://localhost:8060/oauth2/token");
		values.put("clientId", "sathish_ch");
		values.put("clientSecret", "password");
		values.put("userName", "skch2710@gmail.com");
		values.put("password", "S@thi$+b27");
	 */
	public static <T> T getToken(Map<String,String> values, Class<T> responseType) {
		
		String clientCredentials = values.get("clientId")+":"+values.get("clientSecret");
		String encodedCredentials = new String(Base64.getEncoder().encode(clientCredentials.getBytes()));

		// Create the request headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + encodedCredentials);

		// Create the request body with username and password
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("grant_type", "password");
		requestBody.add("username", values.get("userName"));
		requestBody.add("password", values.get("password"));

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

		// Send the POST request to obtain the access token
		ResponseEntity<T> responseEntity = new RestTemplate().exchange(values.get("url"), HttpMethod.POST, requestEntity,
				responseType);

		return responseEntity.getBody();
	}

}
