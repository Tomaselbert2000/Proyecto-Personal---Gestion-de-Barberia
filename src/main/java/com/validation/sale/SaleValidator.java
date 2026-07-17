package com.validation.sale;

import com.dto.product.ProductItemDTO;
import com.dto.sale.SaleCreationDTO;
import com.exceptions.sale.EmptyProductItemListException;
import com.exceptions.sale.InvalidSaleDateTimeException;
import com.exceptions.sale.OrphanBarberServiceException;
import com.exceptions.sale.SaleDateTimeOutOfRangeException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.validation.common.CommonValidationFunctions.validateAnnotationConstraints;
import static com.validation.sale.SaleValidatorConstants.REGISTER_WINDOW_IN_HOURS;

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
