package com.presentation.controller;

import com.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

public abstract class BaseWebController<D> {

    private static final String VALIDATION_TAG = "validationErrors";

    protected String showCreationForm(Model model, Principal principal, D dto) {

        return renderCreationForm(model, principal, dto);
    }

    protected String createEntity(
            @Valid @ModelAttribute D dto,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            String onSuccessRedirectPath
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(VALIDATION_TAG, collectValidationErrors(bindingResult));

            return renderCreationForm(model, principal, dto);
        }

        try {

            executeCreation(dto);

            return onSuccessRedirectPath;

        } catch (BusinessException exception) {

            model.addAttribute(VALIDATION_TAG, List.of(exception.getMessage()));

            return renderCreationForm(model, principal, dto);
        }
    }

    protected abstract void executeCreation(D dto);

    protected abstract String renderCreationForm(Model model, Principal principal, D dto);

    protected abstract void populateCreationForm(Model model);

    protected List<String> collectValidationErrors(BindingResult bindingResult) {

        return bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
    }
}
