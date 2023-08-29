package com.vaadin.clinicfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DoctorDto {
    private String name;
    private String lastname;
    private String specialization;
    private Long userId;
    private String mail;
    private List<Long> calendarEntriesList;
    private List<Long> visitsIdList;
}