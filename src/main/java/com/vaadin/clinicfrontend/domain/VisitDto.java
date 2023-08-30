package com.vaadin.clinicfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class VisitDto {
    private Long patientId;
    private Long doctorId;
    private String description;
    private List<Long> entriesId;
}