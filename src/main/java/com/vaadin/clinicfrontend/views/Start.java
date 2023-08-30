package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.CovidDataDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.clinicfrontend.service.ClinicService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.json.JSONObject;

@PageTitle("Medical Clinic")
@Route(value = "", layout = MainLayout.class)
@PermitAll
@AnonymousAllowed
public class Start extends VerticalLayout {
    ClinicService clinicService;

    public Start(ClinicService clinicService) {
        this.clinicService = clinicService;

        setSpacing(false);

        UserDto userDto = new UserDto(null, "log", "pass", "ADMIN");
        JSONObject jsonObject = new JSONObject(userDto);
        String body = jsonObject.toString();

        H2 header = new H2("Welcome to our Medical Clinic");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Let's hope the development of the page goes well 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");


    }

//    private Component covid(ClinicService clinicService){
//        CovidDataDto covidDataDto = clinicService.
//    }
}