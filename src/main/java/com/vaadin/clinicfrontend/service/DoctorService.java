package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.domain.DoctorDto;
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
public class DoctorService {
    private final RestTemplate restTemplate;

    DoctorDto doctorDto;

    public AdminDto getAdminDto(Long userId){
        return restTemplate.getForObject("http://localhost:8081/admins/" + userId, AdminDto.class);
    }

    public DoctorDto getDoctorDto(Long userId){
        return restTemplate.getForObject("http://localhost:8081/doctors/" + userId, DoctorDto.class);
    }

    public void createDoctor(String name, String lastname, String spec, Long userId, String mail) {
        doctorDto = new DoctorDto(name, lastname, spec, userId, mail, new ArrayList<>(), new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/doctors", setHeader(doctorDto), String.class);
    }

    public HttpEntity<String> setHeader(Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
