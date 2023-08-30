package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final RestTemplate restTemplate;

    DoctorDto doctorDto;

    public List<VisitDto> getVisitsByDoctor(Long userId){
        VisitDto[] response = restTemplate.getForObject("http://localhost:8081/visits/doctor/" + userId, VisitDto[].class);
        return Optional.ofNullable(response)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public CalendarEntryDto getEntry(Long id){
        return restTemplate.getForObject("http://localhost:8081/calendar", CalendarEntryDto.class);
    }

    public List<UserDto> getUsers() {
        UserDto[] response = restTemplate.getForObject("http://localhost:8081/users", UserDto[].class);
        return Optional.ofNullable(response)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public List<DoctorDto> getDoctors(){
        DoctorDto[] response = restTemplate.getForObject("http://localhost:8081/doctors", DoctorDto[].class);
        return Optional.ofNullable(response)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public DoctorDto getDoctorDto(Long userId){
        return restTemplate.getForObject("http://localhost:8081/doctors/user/" + userId, DoctorDto.class);
    }

    public DoctorDto getDoctorById(Long id){
        return restTemplate.getForObject("http://localhost:8081/doctors/" + id, DoctorDto.class);
    }

    public void createDoctor(String name, String lastname, String spec, Long userId, String mail) {
        doctorDto = new DoctorDto(null, name, lastname, spec, userId, mail, new ArrayList<>(), new ArrayList<>());
        restTemplate.postForObject("http://localhost:8081/doctors", setHeader(doctorDto), String.class);
    }

    public void createSchedule(CalendarEntryDto calendarEntryDto){
        restTemplate.postForObject("http://localhost:8081/calendar/schedule", setHeader(calendarEntryDto), String.class);
    }

    public HttpEntity<String> setHeader(Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
