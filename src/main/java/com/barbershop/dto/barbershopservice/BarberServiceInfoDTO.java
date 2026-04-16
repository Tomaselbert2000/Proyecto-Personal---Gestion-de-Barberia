package com.barbershop.dto.barbershopservice;

import com.barbershop.enums.BarberServiceCategory;
import lombok.*;

import static com.barbershop.launcher.constants.ui.messages.GenericStrings.CURRENCY_STRING_ARG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceInfoDTO {

    private Long barberServiceId;
    private String name;
    private Double price;
    private BarberServiceCategory category;
    private String internalNotes;

    @Override
    public String toString() {

        return name + " ~ " + (CURRENCY_STRING_ARG + price);
    }
}
