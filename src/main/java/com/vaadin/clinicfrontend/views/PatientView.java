package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.domain.VisitDto;
import com.vaadin.clinicfrontend.service.DoctorService;
import com.vaadin.clinicfrontend.service.PatientService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "patient", layout = MainLayout.class)
@PageTitle("Patient Panel")
@RolesAllowed({"PATIENT"})
public class PatientView extends VerticalLayout {
    UserService userService;
    PatientService patientService;
    PatientDto patientDto;
    DoctorService doctorService;
    DoctorDto doctorDto;
    VisitDto visitDto;
    Grid<VisitDto> visitsGrid = new Grid<>(VisitDto.class);

    public PatientView(UserService userService, PatientService patientService, DoctorService doctorService) {
        this.doctorService = doctorService;
        this.userService = userService;
        this.patientService = patientService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        patientDto = patientService.getPatientDto(userService.getUser(currentName).getId());
        doctorDto = doctorService.getDoctorById(1L);

        configureVisitsGrid();
        H2 header = new H2("Welcome to the Patient panel, " + patientDto.getName() + " " + patientDto.getLastname());
        Paragraph p1 = new Paragraph("Here are your planned visits:");
        List<DoctorDto> doctors = doctorService.getDoctors();
        List<String> doctorStrings = doctors.stream()
                .map(d -> "" + d.getId() + "." + d.getName() + " " + d.getLastname() + "  Specialization: " + d.getSpecialization())
                .toList();
        configureVisitsGrid();
        ComboBox doctorCombo = new ComboBox<>("doctors", doctorStrings);
        Button searchForDoctor = new Button("See doctor's schedule");


        Paragraph p3 = new Paragraph("Please select a day from the combobox. We will search for a visit for you.");
        List<LocalDate> dates = patientService.checkAvailableDays(doctorDto.getId());
        ComboBox comboBoxAppoint = new ComboBox<>("Available dates", dates);
        Button button = new Button("Check");
        button.addClickListener(click -> {
            patientService.appointVisit(new VisitDto(patientDto.getId(), doctorDto.getId(),
                    "test", new ArrayList<>(Arrays.asList(patientService.proposeEarliestHour((LocalDate) comboBoxAppoint.getValue())))));
            Notification.show("Visit has been successfully appointed!.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        HorizontalLayout appoint = new HorizontalLayout(p3, comboBoxAppoint, button);
        add(header, p1, visitsGrid, doctorCombo, searchForDoctor);
        searchForDoctor.addClickListener(click -> {
            doctorDto = getDoctorFromString(doctorCombo.getValue().toString());
            add(p3, comboBoxAppoint, button);});
    }

    public DoctorDto getDoctorFromString(String str) {
        List<String> tokens = Arrays.stream(str.split("\\D+")).filter(s -> s.length() > 0).toList();
        Long id = (long) Integer.parseInt(tokens.get(0));
        return doctorService.getDoctorById(id);
    }

    private void configureVisitsGrid() {
        visitsGrid.setColumns("description");
        visitsGrid.addColumn(v -> doctorService.getDoctorById(v.getPatientId()).getName()).setHeader("Doctor Name");
        visitsGrid.addColumn(this::findFrom).setHeader("From");
        visitsGrid.addColumn(this::findUntil).setHeader("Until");
        visitsGrid.setHeight("30");

    }

    private LocalTime findFrom(VisitDto visitDto) {
        List<Long> entriesId = visitDto.getEntriesId();
        return doctorService.getEntry(entriesId.get(0)).getFrom();
    }

    private LocalTime findUntil(VisitDto visitDto) {
        List<Long> entriesId = visitDto.getEntriesId();
        return doctorService.getEntry(entriesId.get(entriesId.size() - 1)).getUntil();
    }


}
