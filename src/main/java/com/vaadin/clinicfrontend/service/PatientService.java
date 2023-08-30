package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PatientService {
private final RestTemplate restTemplate;
    PatientDto patientDto;

    public PatientDto getPatientDto(Long userId){
        return restTemplate.getForObject("http://localhost:8081/patients/" + userId, PatientDto.class);
    }

    public void createPatient(String name, String lastname, int pesel, Long userId, String mail) {
        patientDto = new PatientDto(name, lastname, pesel, userId, mail, new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/patients", setHeader(patientDto), String.class);
    }

    public HttpEntity<String> setHeader(Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
