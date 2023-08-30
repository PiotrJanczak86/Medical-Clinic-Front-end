package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.AllergiesDataDto;
import com.vaadin.clinicfrontend.domain.CovidDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalAPIsService {
    private final RestTemplate restTemplate;

    public CovidDataDto getCovidData(){
        return restTemplate.getForObject("http://localhost:8081/external/covid", CovidDataDto.class);
    }

    public AllergiesDataDto getAllergiesData(){
        return restTemplate.getForObject("http://localhost:8081/external/allergy", AllergiesDataDto.class);
    }
}
