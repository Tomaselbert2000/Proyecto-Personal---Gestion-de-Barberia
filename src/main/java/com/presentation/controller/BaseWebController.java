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
import java.util.function.Supplier;

public abstract class BaseWebController<CreationDTO, UpdateDTO> {

    private static final String VALIDATION_TAG = "validationErrors";
    protected static final String ERROR_SUFFIX = "?error=true";

    protected String showCreationForm(Model model, Principal principal, CreationDTO dto) {

        return renderCreationForm(model, principal, dto);
    }

    protected String createEntity(
            @Valid @ModelAttribute CreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            String onSuccessRedirectPath
    ) {

        return processSubmission(bindingResult, model, () -> executeCreation(dto), () -> renderCreationForm(model, principal, dto), onSuccessRedirectPath);
    }

    protected String updateEntity(
            Long entityID,
            @Valid @ModelAttribute UpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            String onSuccessRedirectPath
    ) {

        return processSubmission(bindingResult, model, () -> executeUpdate(entityID, dto), () -> renderUpdateForm(model, entityID, principal, dto), onSuccessRedirectPath);
    }

    protected String deleteEntity(Long entityID, String onSuccessRedirectPath) {

        try {

            executeDeletion(entityID);

            return onSuccessRedirectPath;

        } catch (BusinessException exception) {

            return onSuccessRedirectPath + ERROR_SUFFIX;
        }
    }

    protected abstract UpdateDTO invokeServiceAndReturnDTO(Long id);

    protected abstract void executeCreation(CreationDTO dto);

    protected abstract void executeUpdate(Long entityID, UpdateDTO dto);

    protected abstract void executeDeletion(Long entityID);

    protected abstract String renderCreationForm(Model model, Principal principal, CreationDTO dto);

    protected abstract String renderUpdateForm(Model model, Long entityID, Principal principal, UpdateDTO dto);

    protected abstract void populateCreationForm(Model model);

    protected List<String> collectValidationErrors(BindingResult bindingResult) {

        return bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
    }

    private String processSubmission(
            BindingResult bindingResult,
            Model model,
            Runnable actionToExecute,
            Supplier<String> errorViewProvider,
            String onSuccessRedirectPath
    ) {
        if (bindingResult.hasErrors()) {

            model.addAttribute(VALIDATION_TAG, collectValidationErrors(bindingResult));

            return errorViewProvider.get();
        }

        try {
            actionToExecute.run();

            return onSuccessRedirectPath;

        } catch (BusinessException exception) {

            model.addAttribute(VALIDATION_TAG, List.of(exception.getMessage()));

            return errorViewProvider.get();
        }
    }
}
