package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.service.DoctorService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("doctor")
@PageTitle("Doctor Panel")
@RolesAllowed({"DOCTOR"})
public class DoctorView extends VerticalLayout {
    UserService userService;
    DoctorService doctorService;
    DoctorDto doctorDto;

    public DoctorView(UserService userService, DoctorService doctorService){
        this.userService = userService;
        this.doctorService = doctorService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        doctorDto = doctorService.getDoctorDto(userService.getUser(currentName).getId());

        H2 header = new H2("Welcome to the Doctor panel, " + doctorDto.getName() + " " + doctorDto.getLastname());

        Paragraph p1 = new Paragraph("Here are your planned visits:");
    }
}
