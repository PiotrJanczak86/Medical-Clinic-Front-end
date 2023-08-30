package com.vaadin.clinicfrontend.views;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@Route("doctor")
@PageTitle("Medical Clinic")
@PermitAll
@AnonymousAllowed
public class DoctorView {


}
