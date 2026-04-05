package com.barbershop.mapper.implementation;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodInfoDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.mapper.interfaces.PaymentMethodMapper;
import com.barbershop.model.PaymentMethod;
import com.barbershop.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMethodMapperImpl implements PaymentMethodMapper {

    private final Clock clock;

    public static final Boolean DEFAULT_IS_ACTIVE_VALUE = true;
    public static final String DEFAULT_DESCRIPTION = "No se proporcionó una descripción.";

    public PaymentMethodMapperImpl(Clock clock) {

        this.clock = clock;
    }

    @Override
    public PaymentMethod mapPaymentMethodCreationDtoToPaymentMethod(PaymentMethodCreationDTO creationDTO) {

        if (creationDTO == null) throw new NullMapperInputException();

        if (creationDTO.getDescription().isBlank()) creationDTO.setDescription(DEFAULT_DESCRIPTION);

        return PaymentMethod.builder()
                .name(StringCleaner.formatAsProperName(creationDTO.getName()))
                .description(creationDTO.getDescription())
                .isActive(DEFAULT_IS_ACTIVE_VALUE)
                .createdAt(LocalDate.now(clock))
                .modifierType(creationDTO.getPriceModifierType())
                .priceModifier(creationDTO.getPriceModifier())
                .build();
    }

    @Override
    public PaymentMethod mapPaymentMethodUpdateDtoToPaymentMethod(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO) {

        if (paymentMethod == null || updateDTO == null) throw new NullMapperInputException();

        setUpdatedDataOnEntity(paymentMethod, updateDTO);

        return paymentMethod;
    }

    @Override
    public PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod paymentMethod) {

        if (paymentMethod == null) throw new NullMapperInputException();

        return PaymentMethodInfoDTO.builder()
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .modifierType(paymentMethod.getModifierType())
                .priceModifier(paymentMethod.getPriceModifier())
                .build();
    }

    @Override
    public List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> paymentMethodList) {

        if (paymentMethodList == null) throw new NullMapperInputException();

        return paymentMethodList.stream().map(this::mapPaymentMethodToInfoDTO).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO) {

        if (updateDTO.getNewName() != null) paymentMethod.setName(updateDTO.getNewName());

        if (updateDTO.getNewDescription() != null) {

            if (updateDTO.getNewDescription().isBlank()) {

                paymentMethod.setDescription(DEFAULT_DESCRIPTION);
            } else {

                paymentMethod.setDescription(updateDTO.getNewDescription());
            }
        }

        if (updateDTO.getIsActive() != null) paymentMethod.setIsActive(updateDTO.getIsActive());

        if (updateDTO.getNewModifierType() != null) paymentMethod.setModifierType(updateDTO.getNewModifierType());

        if (updateDTO.getPriceModifier() != null) paymentMethod.setPriceModifier(updateDTO.getPriceModifier());
    }
}
