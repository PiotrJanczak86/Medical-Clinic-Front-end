package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.CalendarEntryDto;
import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.clinicfrontend.domain.VisitDto;
import com.vaadin.clinicfrontend.service.DoctorService;
import com.vaadin.clinicfrontend.service.PatientService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Route(value = "doctor", layout = MainLayout.class)
@PageTitle("Doctor Panel")
@RolesAllowed({"DOCTOR"})
public class DoctorView extends VerticalLayout {
    UserService userService;
    DoctorService doctorService;
    PatientService patientService;
    DoctorDto doctorDto;
    UserDto userDto;
    Grid<VisitDto> visitsGrid = new Grid<>(VisitDto.class);

    public DoctorView(UserService userService, DoctorService doctorService) {
        this.userService = userService;
        this.doctorService = doctorService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        doctorDto = doctorService.getDoctorDto(userService.getUser(currentName).getId());
        userDto = userService.getUser(currentName);

        H2 header = new H2("Welcome to the Doctor panel, " + doctorDto.getName() + " " + doctorDto.getLastname());
        Paragraph p1 = new Paragraph("Here are your planned visits:");
        configureVisitsGrid();
        Paragraph p2 = new Paragraph("Please set your calendar for the next month:");

        add(header, p1, visitsGrid, p2, scheduleCreator());

    }

    private void configureVisitsGrid() {
//        visitsGrid.setSizeFull();
        visitsGrid.setColumns("description");
        visitsGrid.addColumn(v -> patientService.getPatientById(v.getPatientId()).getName()).setHeader("Name");
        visitsGrid.addColumn(v -> patientService.getPatientById(v.getPatientId()).getLastname()).setHeader("Lastname");
        visitsGrid.addColumn(this::findFrom).setHeader("From");
        visitsGrid.addColumn(this::findUntil).setHeader("Until");
//        visitsGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private Component scheduleCreator() {

        DatePicker datePicker = new DatePicker("Select a date:");
        DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat("yyyy-MM-dd");

        datePicker.setI18n(singleFormatI18n);


        TimePicker timePicker = new TimePicker();
        timePicker.setLabel("Start");
        timePicker.setHelperText("Open 8:00-16:00");
        timePicker.setStep(Duration.ofMinutes(15));
        timePicker.setValue(LocalTime.of(8, 0));
        timePicker.setMin(LocalTime.of(8, 0));
        timePicker.setMax(LocalTime.of(17, 0));
        timePicker.addValueChangeListener(event -> {
            LocalTime value = event.getValue();
            String errorMessage = null;
            if (value != null) {
                if (value.compareTo(timePicker.getMin()) < 0) {
                    errorMessage = "Too early, choose another time";
                } else if (value.compareTo(timePicker.getMax()) > 0) {
                    errorMessage = "Too late, choose another time";
                }
            }
            timePicker.setErrorMessage(errorMessage);
        });

        TimePicker timePicker1 = new TimePicker();
        timePicker1.setLabel("Finish");
        timePicker1.setHelperText("Open 8:00-16:00");
        timePicker1.setStep(Duration.ofMinutes(15));
        timePicker1.setValue(LocalTime.of(17, 0));
        timePicker1.setMin(LocalTime.of(8, 0));
        timePicker1.setMax(LocalTime.of(17, 0));
        timePicker1.addValueChangeListener(event -> {
            LocalTime value = event.getValue();
            String errorMessage = null;
            if (value != null) {
                if (value.compareTo(timePicker.getMin()) < 0) {
                    errorMessage = "Too early, choose another time";
                } else if (value.compareTo(timePicker.getMax()) > 0) {
                    errorMessage = "Too late, choose another time";
                }
            }
            timePicker.setErrorMessage(errorMessage);

        });
        Button button = new Button("Submit");

        HorizontalLayout hl = new HorizontalLayout(datePicker, timePicker, timePicker1, button);
        button.addClickListener(click -> doctorService.createSchedule(new CalendarEntryDto(null, doctorDto.getId(), datePicker.getValue(), timePicker.getValue(), timePicker1.getValue(), null)));
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return hl;
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
