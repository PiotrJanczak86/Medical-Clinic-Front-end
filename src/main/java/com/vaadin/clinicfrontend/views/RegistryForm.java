package com.vaadin.clinicfrontend.views;

import com.vaadin.clinicfrontend.domain.AdminDto;
import com.vaadin.clinicfrontend.domain.DoctorDto;
import com.vaadin.clinicfrontend.domain.PatientDto;
import com.vaadin.clinicfrontend.domain.UserDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("registryForm")
@AnonymousAllowed
public class RegistryForm extends HorizontalLayout {
    UserDto userData;
    PatientDto patientData;
    DoctorDto doctorData;
    AdminDto adminData;
    Component patientForm = registerPatient();
    Component doctorForm = registerDoctor();
    Component adminForm = registerAdmin();

    public RegistryForm() {


        add(registerUser(), patientForm, doctorForm, adminForm);
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
            checkRole(role.getValue());
        });
        VerticalLayout form = new VerticalLayout(login, pass, role, button);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return form;
    }

    private Component registerPatient() {
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        TextField pesel = new TextField("Pesel");
        TextField mail = new TextField("Email");
        Button register = new Button("Register");
        register.addClickListener(click -> patientData = new PatientDto(name.getValue(), lastname.getValue(),
                Integer.parseInt(pesel.getValue()), null, mail.getValue(), null));
        VerticalLayout form  = new VerticalLayout(name, lastname, pesel, mail, register);
        form.setDefaultHorizontalComponentAlignment(Alignment.START);
        return form;
    }

    private Component registerDoctor(){
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        TextField spec = new TextField("Specialization");
        TextField mail = new TextField("Email");
        Button register = new Button("Register");
        register.addClickListener(click -> doctorData = new DoctorDto(name.getValue(), lastname.getValue(),
                spec.getValue(), null, mail.getValue(), null, null));
        VerticalLayout form  = new VerticalLayout(name, lastname, spec, mail, register);
        form.setDefaultHorizontalComponentAlignment(Alignment.START);
        return form;
    }

    private Component registerAdmin(){
        TextField name = new TextField("Name");
        TextField lastname = new TextField("Lastname");
        Button register = new Button("Register");
        register.addClickListener((click -> adminData = new AdminDto(name.getValue(), lastname.getValue(), null)));
        VerticalLayout form  = new VerticalLayout(name, lastname,register);
        form.setDefaultHorizontalComponentAlignment(Alignment.START);
        return form;

    }
    private void checkRole(String role) {
        switch (role) {
            case "PATIENT" -> {patientForm.setVisible(true); doctorForm.setVisible(false); adminForm.setVisible(false);}
            case "DOCTOR" -> {doctorForm.setVisible(true); patientForm.setVisible(false); adminForm.setVisible(false);}
            case "ADMIN" -> {adminForm.setVisible(true); patientForm.setVisible(false); doctorForm.setVisible(false);}
        }
    }

}
