package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.AllergiesDataDto;
import com.vaadin.clinicfrontend.domain.CovidDataDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.clinicfrontend.service.ExternalAPIsService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    ExternalAPIsService externalAPIsService;
    UserService userService;

    public Start(ExternalAPIsService externalAPIsService, UserService userService) {
        this.externalAPIsService = externalAPIsService;
        this.userService = userService;

        setSpacing(false);

        UserDto userDto = new UserDto(null, "log", "pass", "ADMIN");
        JSONObject jsonObject = new JSONObject(userDto);
        String body = jsonObject.toString();

        HorizontalLayout stats = new HorizontalLayout(covid((externalAPIsService)), allergies(externalAPIsService));

        H2 header = new H2("Welcome to our Medical Clinic");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Our page is still under development, please be understanding 🤗"));
        add(stats);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

    }

    private Component covid(ExternalAPIsService service) {
        CovidDataDto covidDataDto = service.getCovidData();

        Html html = new Html("<span>" + "Covid may be declining, but it's still here.<br/>" +
                "Please visit our clinic to get vaccinated!<br/>" +
                "Some statistics from today regarding Poland:" + "</span>");

        Paragraph deaths = new Paragraph("Deaths: " + covidDataDto.getDeaths());
        Paragraph cases = new Paragraph("New cases: " + covidDataDto.getCases());
        Paragraph critical = new Paragraph("Patients in critical state: " + covidDataDto.getCritical());
        VerticalLayout covid = new VerticalLayout(html, deaths, cases, critical);
        return covid;
    }

    private Component allergies(ExternalAPIsService service) {
        AllergiesDataDto allergiesDataDto = service.getAllergiesData();

        Html html = new Html("<span>" + "Visit our clinic to get treated for allergies.<br/>" +
                "Statistics from today regarding Warsaw:<br/>" +
                "(values shown in units per cubic meter of air)" + "</span>");

        Paragraph grass = new Paragraph("Grass: " + allergiesDataDto.getGrassValue() + " Category: " + allergiesDataDto.getGrass());
        Paragraph mold = new Paragraph("Mold: " + allergiesDataDto.getMoldValue() + " Category: " + allergiesDataDto.getMold());
        Paragraph ragweed = new Paragraph("Ragweed: " + allergiesDataDto.getRagweedValue() + " Category: " + allergiesDataDto.getRagweed());
        Paragraph tree = new Paragraph("Tree allergens: " + allergiesDataDto.getTreeValue() + " Category: " + allergiesDataDto.getTree());
        VerticalLayout allergies = new VerticalLayout(html, grass, mold, ragweed, tree);
        return allergies;
    }
}