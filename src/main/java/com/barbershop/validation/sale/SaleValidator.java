package com.barbershop.validation.sale;

import com.barbershop.dto.product.ProductItemDTO;
import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.exceptions.sale.*;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;
import static com.barbershop.validation.sale.SaleValidatorConstants.REGISTER_WINDOW_IN_HOURS;

@Component
@RequiredArgsConstructor
public class SaleValidator {

    private final Clock clock;
    private final Validator validatorEngine;

    public void validateDTO(SaleCreationDTO creationDTO) {

        checkIfDtoIsNull(creationDTO);

        validateAnnotationConstraints(validatorEngine, creationDTO);

        checkIfSaleIsEmpty(creationDTO.getBarberServiceID(), creationDTO.getProductsDetail());

        validateBarberServiceAndEmployee(creationDTO.getEmployeeID(), creationDTO.getBarberServiceID());

        validateSaleDateTime(creationDTO.getDateAndTime());
    }

    private void validateSaleDateTime(LocalDateTime dateAndTime) {

        if (dateAndTime.isAfter(LocalDateTime.now(clock))) throw new InvalidSaleDateTimeException();

        if (dateAndTime.isBefore(LocalDateTime.now(clock).minusHours(REGISTER_WINDOW_IN_HOURS)))
            throw new SaleDateTimeOutOfRangeException();
    }

    private void validateBarberServiceAndEmployee(Long employeeID, Long barberServiceID) {

        if (employeeID == null && barberServiceID != null) throw new OrphanBarberServiceException();
    }

    private void checkIfSaleIsEmpty(Long barberServiceID, List<ProductItemDTO> productsDetail) {

        if (barberServiceID == null && (productsDetail == null || productsDetail.isEmpty()))
            throw new EmptyProductItemListException();
    }
}
