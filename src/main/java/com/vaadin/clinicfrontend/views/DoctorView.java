package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.VisitDto;
import com.vaadin.clinicfrontend.service.DoctorService;
import com.vaadin.clinicfrontend.service.PatientService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Route(value = "doctor", layout = MainLayout.class)
@PageTitle("Doctor Panel")
@RolesAllowed({"DOCTOR"})
public class DoctorView extends VerticalLayout {
    UserService userService;
    DoctorService doctorService;
    PatientService patientService;
    DoctorDto doctorDto;
    Grid<VisitDto> visitsGrid = new Grid<>(VisitDto.class);
    Locale polishLocale = new Locale("pl", "PL");



    public DoctorView(UserService userService, DoctorService doctorService){
        this.userService = userService;
        this.doctorService = doctorService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        doctorDto = doctorService.getDoctorDto(userService.getUser(currentName).getId());

        H2 header = new H2("Welcome to the Doctor panel, " + doctorDto.getName() + " " + doctorDto.getLastname());
        Paragraph p1 = new Paragraph("Here are your planned visits:");
        configureVisitsGrid();
        Paragraph p2 = new Paragraph("Please set your calendar for the next month:");


        add(header, p1, visitsGrid, p2, scheduleCreator());


    }

    private void configureVisitsGrid(){
//        visitsGrid.setSizeFull();
        visitsGrid.setColumns("description");
        visitsGrid.addColumn(v -> patientService.getPatientById(v.getPatientId()).getName()).setHeader("Name");
        visitsGrid.addColumn(v -> patientService.getPatientById(v.getPatientId()).getLastname()).setHeader("Lastname");
        visitsGrid.addColumn(this::findFrom).setHeader("From");
        visitsGrid.addColumn(this::findFrom).setHeader("Until");
//        visitsGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private Component scheduleCreator(){
        DatePicker datePicker = new DatePicker("Select a date:");
        datePicker.setLocale(polishLocale);
        HorizontalLayout hl = new HorizontalLayout(datePicker);
        return hl;
    }

    private LocalTime findFrom(VisitDto visitDto){
        List<Long> entriesId = visitDto.getEntriesId();
        return doctorService.getEntry(entriesId.get(0)).getFrom();
    }
    private LocalTime findUntil(VisitDto visitDto){
        List<Long> entriesId = visitDto.getEntriesId();
        return doctorService.getEntry(entriesId.get(entriesId.size()-1)).getUntil();
    }
}
