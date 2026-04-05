package com.barbershop.exceptions.appointment;

public class InvalidAppointmentUpdateException extends RuntimeException{

    @Override
    public String getMessage(){

        return "No es posible actualizar turnos cancelados y/o finalizados, o bien establecer un turno de estado 'Reprogramado' como 'Programado'.";
    }
}
