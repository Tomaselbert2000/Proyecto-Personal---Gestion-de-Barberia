package com.mapper.implementation;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import com.utils.strings.StringCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
@RequiredArgsConstructor
public class PaymentMethodMapperImpl implements PaymentMethodMapper {

    public static final Boolean DEFAULT_IS_ACTIVE_VALUE = true;
    public static final String DEFAULT_DESCRIPTION = "No se proporcionó una descripción.";
    private final Clock clock;

    @Override
    public PaymentMethod mapPaymentMethodCreationDtoToPaymentMethod(PaymentMethodCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        if (dto.getDescription().isBlank()) dto.setDescription(DEFAULT_DESCRIPTION);

        return PaymentMethod.builder()
                .name(StringCleaner.formatAsProperName(dto.getName()))
                .description(dto.getDescription())
                .isActive(DEFAULT_IS_ACTIVE_VALUE)
                .createdAt(LocalDate.now(clock))
                .modifierType(dto.getPriceModifierType())
                .priceModifier(dto.getPriceModifier())
                .build();
    }

    @Override
    public PaymentMethod mapPaymentMethodUpdateDtoToPaymentMethod(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO) {

        checkIfMapperInputIsNull(paymentMethod, updateDTO);

        setUpdatedDataOnEntity(paymentMethod, updateDTO);

        return paymentMethod;
    }

    @Override
    public PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod paymentMethod) {

        checkIfMapperInputIsNull(paymentMethod);

        return PaymentMethodInfoDTO.builder()
                .id(paymentMethod.getPaymentMethodID())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .modifierType(paymentMethod.getModifierType())
                .priceModifier(paymentMethod.getPriceModifier())
                .isActive(paymentMethod.getIsActive())
                .build();
    }

    @Override
    public List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> paymentMethodList) {

        checkIfMapperInputIsNull(paymentMethodList);

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
