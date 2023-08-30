package com.vaadin.clinicfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllergiesDataDto {
    private String grass;
    private Long grassValue;
    private String mold;
    private Long MoldValue;
    private String ragweed;
    private Long ragweedValue;
    private String tree;
    private Long treeValue;
}