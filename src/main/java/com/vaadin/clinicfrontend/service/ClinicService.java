package com.vaadin.clinicfrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final RestTemplate restTemplate;
    UserDto userDto;
    PatientDto patientDto;
    DoctorDto doctorDto;
    AdminDto adminDto;

    public void createUser(String login, String password, String role) {

        userDto = new UserDto(null, login, password, role);
        JSONObject jsonObject = new JSONObject(userDto);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.postForObject("http://localhost:8081/users", entity, String.class);
    }

    public boolean checkIfUserExists(String login) {
        return (Boolean.TRUE.equals(restTemplate.getForObject("http://localhost:8081/users/check/" + login, Boolean.class)));
    }

    public void createPatient(String name, String lastname, int pesel, Long userId, String mail){
        patientDto = new PatientDto(name,lastname,pesel,userId,mail,new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/patients", setHeader(patientDto), String.class);
    }

    public void createDoctor(String name, String lastname, String spec, Long userId, String mail) {
        doctorDto = new DoctorDto(name, lastname, spec, userId, mail, new ArrayList<>(), new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/doctors", setHeader(doctorDto), String.class);
    }
    public void createAdmin(String name, String lastname, Long userId) {
        adminDto = new AdminDto(name, lastname, userId);
        restTemplate.postForObject("http://localhost:8081/admins", setHeader(adminDto), String.class);
    }

    public UserDto getUser(String login){
        return restTemplate.getForObject("http://localhost:8081/users/"+login,UserDto.class);
    }

    public HttpEntity<String> setHeader(Object object){
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

}