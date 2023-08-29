package com.vaadin.clinicfrontend.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
//    public RegisterUser(ClinicService clinicService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
//        this.clinicService = clinicService;
//
//        role.setItems(roles);
//        add(login, pass, role, button, button1);
//        UserDetails user1 =
//                User.withUsername("user1")
//                        .password("{noop}user1")
//                        .roles("USER")
//                        .build();
//        button.addClickListener(click -> this.clinicService.createUser(login.getValue(), pass.getValue(), role.getValue()));
//        button1.addClickListener(c -> inMemoryUserDetailsManager.createUser(user1));
//    }
}