package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.service.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;


public class MainLayout extends AppLayout {

    private SecurityService securityService;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;

        H2 logo = new H2("Medical Clinic");
        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click ->
                    securityService.logout());
            header = new HorizontalLayout(logo, logout);
        } else {
            Button login = new Button("LogIn", click -> UI.getCurrent().navigate("login"));
            Button register = new Button("Register", click -> UI.getCurrent().navigate("registryForm"));
            header = new HorizontalLayout(logo, register, login);
        }
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setFlexGrow(0.98, logo);
        addToNavbar(header);
    }
}