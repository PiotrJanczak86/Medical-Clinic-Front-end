package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.service.AdminService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin panel")
@RolesAllowed({"ADMIN"})
public class AdminView extends VerticalLayout {

    AdminService adminService;
    UserService userService;
    AdminDto adminDto;

    public AdminView(AdminService adminService, UserService userService){
        this.adminService = adminService;
        this.userService = userService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        adminDto = adminService.getAdminDto(userService.getUser(currentName).getId());

        H2 header = new H2("Welcome to the Admin panel, " + adminDto.getName() + " " + adminDto.getLastname());
        Pre logs = new Pre("Here are your logs:\n" + adminDto.getLogs());
        add(header, logs);
    }
}