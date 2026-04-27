package com.barbershop.launcher.controller.interfaces;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;

public interface BarberServiceControllerViewFunctions {

    void configureButtonActions(BarberServiceInfoDTO... infoDTO);

    void resetForm(BarberServiceInfoDTO... infoDTO);
}
