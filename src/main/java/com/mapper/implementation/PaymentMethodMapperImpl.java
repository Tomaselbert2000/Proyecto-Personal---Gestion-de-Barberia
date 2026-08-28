package com.mapper.implementation;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import com.utils.strings.StringCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
@RequiredArgsConstructor
public class PaymentMethodMapperImpl implements PaymentMethodMapper {

    public static final Boolean DEFAULT_IS_ACTIVE_VALUE = true;
    public static final String DEFAULT_DESCRIPTION = "No se proporcionó una descripción.";
    private final Clock clock;

    @Override
    public PaymentMethod mapPaymentMethodCreationDtoToEntity(PaymentMethodCreationDTO dto) {

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
    public PaymentMethod mapPaymentMethodUpdateDtoToEntity(PaymentMethod entity, PaymentMethodUpdateDTO dto) {

        checkIfMapperInputIsNull(entity, dto);

        setUpdatedDataOnEntity(entity, dto);

        return entity;
    }

    @Override
    public PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod entity) {

        checkIfMapperInputIsNull(entity);

        return PaymentMethodInfoDTO.builder()
                .id(entity.getPaymentMethodID())
                .name(entity.getName())
                .description(entity.getDescription())
                .modifierType(entity.getModifierType())
                .priceModifier(entity.getPriceModifier())
                .isActive(entity.getIsActive())
                .build();
    }

    @Override
    public List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> entityList) {

        return MapperHelper.mapList(entityList, this::mapPaymentMethodToInfoDTO);
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
