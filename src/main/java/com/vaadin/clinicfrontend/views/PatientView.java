package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.service.PatientService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "patient", layout = MainLayout.class)
@PageTitle("Patient Panel")
@RolesAllowed({"PATIENT"})
public class PatientView extends VerticalLayout {
    UserService userService;
    PatientService patientService;
    PatientDto patientDto;

    public PatientView(UserService userService, PatientService patientService){
        this.userService = userService;
        this.patientService = patientService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        patientDto = patientService.getPatientDto(userService.getUser(currentName).getId());

        H2 header = new H2("Welcome to the Patient panel, " + patientDto.getName() + " " + patientDto.getLastname());
    }
}
