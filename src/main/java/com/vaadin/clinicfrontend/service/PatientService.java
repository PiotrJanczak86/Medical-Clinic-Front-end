package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.DateDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.domain.VisitDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {
private final RestTemplate restTemplate;
    PatientDto patientDto;

    public PatientDto getPatientDto(Long userId){
        return restTemplate.getForObject("http://localhost:8081/patients/user" + userId, PatientDto.class);
    }

    public PatientDto getPatientById(Long id){
        return restTemplate.getForObject("http://localhost:8081/patients" + id, PatientDto.class);
    }

    public void createPatient(String name, String lastname, int pesel, Long userId, String mail) {
        patientDto = new PatientDto(null,name, lastname, pesel, userId, mail, new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/patients", setHeader(patientDto), String.class);
    }

    public List<LocalDate> checkAvailableDays(Long doctorId){
        LocalDate[] response = restTemplate.getForObject("http://localhost:8081/calendar/availableDays/" + doctorId, LocalDate[].class);
        return Optional.ofNullable(response)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public Long proposeEarliestHour(LocalDate date){
        return restTemplate.postForObject("http://localhost:8081/calendar/propose/", setHeaderDate(date), Long.class);
    }

    public void appointVisit(VisitDto visitDto){
        restTemplate.postForObject("http://localhost:8081/visits", setHeader(visitDto), String.class);
    }

    public HttpEntity<String> setHeaderDate(LocalDate date) {

        DateDto dateDto = new DateDto(date);
        JSONObject jsonObject = new JSONObject(dateDto);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(body);
        return new HttpEntity<>(body, headers);
    }

    public HttpEntity<String> setHeader(Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(body);
        return new HttpEntity<>(body, headers);

    }
}