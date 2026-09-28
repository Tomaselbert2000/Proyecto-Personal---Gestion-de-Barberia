package com.presentation.controller;

import com.exceptions.BusinessException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final String DATA_INTEGRITY_ERROR = "La operación solicitada incurre en un error de integridad de base de datos";
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

        try {

            return processSubmission(bindingResult, model, () -> executeCreation(dto), () -> renderCreationForm(model, principal, dto), onSuccessRedirectPath);

        } catch (BusinessException | ConstraintViolationException | DataIntegrityViolationException e) {

            return onSuccessRedirectPath + ERROR_SUFFIX;
        }
    }

    protected String updateEntity(
            Long entityID,
            @Valid @ModelAttribute UpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            String onSuccessRedirectPath
    ) {

        try {
            return processSubmission(bindingResult, model, () -> executeUpdate(entityID, dto), () -> renderUpdateForm(model, entityID, principal, dto), onSuccessRedirectPath);

        } catch (BusinessException | ConstraintViolationException | DataIntegrityViolationException e) {

            return onSuccessRedirectPath + ERROR_SUFFIX;
        }
    }

    protected String deleteEntity(Long entityID, String onSuccessRedirectPath) {

        try {

            executeDeletion(entityID);

            return onSuccessRedirectPath;

        } catch (BusinessException | DataIntegrityViolationException exception) {

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

        } catch (BusinessException | ConstraintViolationException exception) {

            model.addAttribute(VALIDATION_TAG, List.of(exception.getMessage()));

            return errorViewProvider.get();

        } catch (DataIntegrityViolationException exception) {

            model.addAttribute(VALIDATION_TAG, List.of(DATA_INTEGRITY_ERROR));

            return errorViewProvider.get();
        }
    }
}
