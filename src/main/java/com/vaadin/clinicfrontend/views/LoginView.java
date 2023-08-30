package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    UserService userService;
    InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private LoginForm login = new LoginForm();

    public LoginView(InMemoryUserDetailsManager inMemoryUserDetailsManager, UserService userService) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.userService = userService;

        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");
        addUsersToMemory();
        add(new H2("Welcome again"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }

    }

    public void addUsersToMemory() {
        List<UserDto> users = userService.getUsers();
        for (UserDto user : users) {
            UserDetails userToAdd =
                    User.withUsername(user.getLogin())
                            .password("{noop}" + user.getPassword())
                            .roles(user.getRole())
                            .build();
            if (!inMemoryUserDetailsManager.userExists(user.getLogin()))
                inMemoryUserDetailsManager.createUser(userToAdd);
        }
    }
}