package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.service.SecurityService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class MainLayout extends AppLayout {

    UserService userService;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String panel;

    public MainLayout(@Autowired SecurityService securityService, UserService userService) {
        this.userService = userService;


        H2 logo = new H2("Medical Clinic");
        HorizontalLayout header;

        Button button = new Button("GO TO PANEL");
        button.addClickListener(click -> UI.getCurrent().navigate(panel));


        if (securityService.getAuthenticatedUser() != null) {
            navigate();
            Button logout = new Button("Logout", click ->
                    securityService.logout());
            header = new HorizontalLayout(logo, button, logout);
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

    public void navigate() {
        String currentRole = userService.getUser(authentication.getName()).getRole();
        if (currentRole.equals("ADMIN")) {
            panel="admin";
        }
        if (currentRole.equals("DOCTOR")) {
            panel="doctor";
        }
        if (currentRole.equals("PATIENT")) {
            panel="patient";
        }
    }
}