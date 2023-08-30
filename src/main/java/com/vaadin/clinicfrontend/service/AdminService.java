package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.AdminDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final RestTemplate restTemplate;
    AdminDto adminDto;



    public void createAdmin(String name, String lastname, Long userId) {
        adminDto = new AdminDto(name, lastname, userId);
        restTemplate.postForObject("http://localhost:8081/admins", setHeader(adminDto), String.class);
    }

    public HttpEntity<String> setHeader(Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
