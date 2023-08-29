package com.vaadin.clinicfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdminDto {
    private String name;
    private String lastname;
    private Long userId;
}