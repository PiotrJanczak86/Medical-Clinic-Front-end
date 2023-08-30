package com.vaadin.clinicfrontend.service;

import com.vaadin.clinicfrontend.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    UserDto userDto;

    public UserDto getUser(String login) {
        return restTemplate.getForObject("http://localhost:8081/users" + login, UserDto.class);
    }
    public List<UserDto> getUsers() {
        UserDto[] response = restTemplate.getForObject("http://localhost:8081/users", UserDto[].class);
        return Optional.ofNullable(response)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

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
}
