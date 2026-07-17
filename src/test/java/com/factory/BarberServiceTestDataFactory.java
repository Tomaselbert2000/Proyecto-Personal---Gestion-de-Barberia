package com.factory;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.model.BarberService;

import static com.test_constant.BarberServiceTestConstants.CreationValidData.*;
import static com.test_constant.BarberServiceTestConstants.MapperTestData.*;
import static com.test_constant.BarberServiceTestConstants.UpdateValidData.*;

public class BarberServiceTestDataFactory {

    private BarberServiceTestDataFactory() {
    }

    public static BarberServiceCreationDTO buildValidBarberServiceCreationDTO() {

        return BarberServiceCreationDTO.builder()
                .name(BARBER_SERVICE_NAME)
                .price(BARBER_SERVICE_PRICE)
                .serviceCategory(BARBER_SERVICE_CATEGORY)
                .internalNotes(INTERNAL_NOTES)
                .build();
    }

    public static BarberServiceUpdateDTO buildValidBarberServiceUpdateDTO() {

        return BarberServiceUpdateDTO.builder()
                .name(NEW_BARBER_SERVICE_NAME)
                .price(NEW_PRICE)
                .serviceCategory(NEW_CATEGORY)
                .internalNotes(NEW_INTERNAL_NOTES)
                .build();
    }

    public static BarberService buildValidBarberService() {

        return BarberService.builder()
                .barbershopServiceID(BARBER_SERVICE_ID)
                .name(BARBER_SERVICE_NAME)
                .price(BARBER_SERVICE_PRICE)
                .serviceCategory(BARBER_SERVICE_CATEGORY)
                .registrationTimestamp(BARBER_SERVICE_REGISTRATION_TIMESTAMP)
                .modifiedDate(BARBER_SERVICE_MODIFICATION_TIMESTAMP)
                .internalNotes(INTERNAL_NOTES)
                .build();
    }
}
