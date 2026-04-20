package com.barbershop.dto.barbershopservice;

import com.barbershop.enums.BarberServiceCategory;
import lombok.*;

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

        return name;
    }
}
