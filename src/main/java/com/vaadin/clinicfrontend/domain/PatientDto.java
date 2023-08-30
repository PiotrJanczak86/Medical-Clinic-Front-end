package com.vaadin.clinicfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PatientDto {
    private Long id;
    private String name;
    private String lastname;
    private int pesel;
    private Long userId;
    private String mail;
    private List<Long> visitsIdList;
}