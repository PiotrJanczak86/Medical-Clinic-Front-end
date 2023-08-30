package com.vaadin.clinicfrontend.views;

import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("Admin")
@RolesAllowed("ADMIN")
public class AdminView {

}
