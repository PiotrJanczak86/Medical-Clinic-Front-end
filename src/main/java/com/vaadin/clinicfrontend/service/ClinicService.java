package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final RestTemplate restTemplate;
    UserDto userDto;

    public void createUser(String login, String password, String role) {

        userDto = new UserDto(null, login, password, role);
        JSONObject jsonObject = new JSONObject(userDto);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.postForObject("http://localhost:8081/users", entity, String.class);
    }
}