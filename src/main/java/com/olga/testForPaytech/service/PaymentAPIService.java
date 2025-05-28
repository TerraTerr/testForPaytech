package com.olga.testForPaytech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.olga.testForPaytech.constants.Constants.*;

public class PaymentAPIService {

    public ResponseEntity<String> sentPostRequest (double amount) throws HttpClientErrorException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, AUTHORIZATION_BEARER);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(PAYMENT_TYPE, PAYMENT_TYPE_DEPOSIT);
        requestBody.put(AMOUNT, amount);
        requestBody.put(CURRENCY, CURRENCY_EUR);

        Map<String, String> customer = new HashMap<>();
        customer.put(REFERENCE_ID, REFERENCE_ID_VALUE);
        requestBody.put(CUSTOMER, customer);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(URL, requestEntity, String.class);
    }

    public String parseAnswer (String answer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(answer, new TypeReference<>() {});
        Map<String, Object> result = (Map<String, Object>) map.get(RESULT);

        return (String) result.get(REDIRECT_URL);
    }

}
