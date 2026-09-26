 package com.presentation.controller.web;


import org.springframework.ui.Model;


import java.security.Principal;


public interface WebController<T> {


String renderCreationForm(Model model, Principal principal, T dto);


void populateCreationCatalog(Model model);

}