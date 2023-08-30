package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.clinicfrontend.service.AdminService;
import com.vaadin.clinicfrontend.service.DoctorService;
import com.vaadin.clinicfrontend.service.PatientService;
import com.vaadin.clinicfrontend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("registryForm")
@AnonymousAllowed
public class RegistryForm extends HorizontalLayout {
    UserService userService;
    PatientService patientService;
    DoctorService doctorService;
    AdminService adminService;
    InMemoryUserDetailsManager inMemoryUserDetailsManager;
    UserDto userData;
    PatientDto patientData;
    DoctorDto doctorData;
    AdminDto adminData;
    Component userForm = registerUser();
    Component patientForm = registerPatient();
    Component doctorForm = registerDoctor();
    Component adminForm = registerAdmin();

    public RegistryForm(UserService userService, PatientService patientService, DoctorService doctorService, AdminService adminService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.adminService = adminService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;


        add(userForm, patientForm, doctorForm, adminForm);
        patientForm.setVisible(false);
        doctorForm.setVisible(false);
        adminForm.setVisible(false);
    }

    private Component registerUser() {
        TextField login = new TextField("login");
        TextField pass = new TextField("password");
        ComboBox<String> role = new ComboBox<>("role");
        List<String> roles = new ArrayList<>(Arrays.asList("PATIENT", "DOCTOR", "ADMIN"));
        Button button = new Button("submit");
        role.setItems(roles);
        role.setValue(roles.iterator().next());
        button.addClickListener(click -> {
            userData = new UserDto(null, login.getValue(), pass.getValue(), role.getValue());
            if (!checkIfExists(login.getValue())) {
                checkRole(role.getValue());
            }
        });
        VerticalLayout form = new VerticalLayout(new Paragraph("Welcome to the registration form! Please fill the initial info:"), login, pass, role, button);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return form;
    }

    private Component registerPatient() {
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        TextField pesel = new TextField("Pesel");
        TextField mail = new TextField("Email");
        Button register = new Button("Register");
        register.addClickListener(click -> {
            patientData = new PatientDto(name.getValue(), lastname.getValue(),
                    Integer.parseInt(pesel.getValue()), null, mail.getValue(), null);
            if (!checkIfExists(userData.getLogin())) {
                userService.createUser(userData.getLogin(), userData.getPassword(), userData.getRole());
                userData = userService.getUser(userData.getLogin());
                patientService.createPatient(patientData.getName(), patientData.getLastname(), patientData.getPesel(), userData.getId(), patientData.getMail());
                Notification.show("Registration complete! You can now log in.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                putUserIntoMemory(userData);
                UI.getCurrent().navigate("");
            }
        });
        VerticalLayout form = new VerticalLayout(new Paragraph("We just need couple more information to complete the process:"), name, lastname, pesel, mail, register);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return form;
    }

    private Component registerDoctor() {
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        TextField spec = new TextField("Specialization");
        TextField mail = new TextField("Email");
        Button register = new Button("Register");
        register.addClickListener(click -> {
            doctorData = new DoctorDto(name.getValue(), lastname.getValue(),
                    spec.getValue(), null, mail.getValue(), null, null);
            if (!checkIfExists(userData.getLogin())) {
                userService.createUser(userData.getLogin(), userData.getPassword(), userData.getRole());
                userData = userService.getUser(userData.getLogin());
                doctorService.createDoctor(doctorData.getName(), doctorData.getLastname(), doctorData.getSpecialization(), userData.getId(), doctorData.getMail());
                Notification.show("Registration complete! You can now log in.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                putUserIntoMemory(userData);
                UI.getCurrent().navigate("");
            }
        });
        VerticalLayout form = new VerticalLayout(new Paragraph("We just need couple more information to complete the process:"), name, lastname, spec, mail, register);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return form;
    }

    private Component registerAdmin() {
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        Button register = new Button("Register");
        register.addClickListener(click -> {
            adminData = new AdminDto(name.getValue(), lastname.getValue(), null);
            if (!checkIfExists(userData.getLogin())) {
                userService.createUser(userData.getLogin(), userData.getPassword(), userData.getRole());
                userData = userService.getUser(userData.getLogin());
                adminService.createAdmin(adminData.getName(), adminData.getLastname(), userData.getId());
                Notification.show("Registration complete! You can now log in.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                putUserIntoMemory(userData);
                UI.getCurrent().navigate("");
            }
        });
        VerticalLayout form = new VerticalLayout(new Paragraph("We just need couple more information to complete the process:"), name, lastname, register);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return form;

    }

    private boolean checkIfExists(String login) {
        if (userService.checkIfUserExists(login)) {
            Notification.show("This login name is already taken! Please select a new one.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return true;
        }
        return false;
    }

    private void checkRole(String role) {


        switch (role) {
            case "PATIENT" -> {
                patientForm.setVisible(true);
                userForm.setVisible(false);
            }
            case "DOCTOR" -> {
                doctorForm.setVisible(true);
                userForm.setVisible(false);
            }
            case "ADMIN" -> {
                adminForm.setVisible(true);
                userForm.setVisible(false);
            }
        }
    }

    private void putUserIntoMemory(UserDto userDto) {
        UserDetails user = User.withUsername(userDto.getLogin())
                .password("{noop}" + userDto.getPassword())
                .roles(userDto.getRole())
                .build();
        inMemoryUserDetailsManager.createUser(user);
    }
}