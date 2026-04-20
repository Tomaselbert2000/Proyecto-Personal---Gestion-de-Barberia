package com.barbershop.model;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exceptions.appointment.InvalidAppointmentUpdateException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.barbershop.enums.AppointmentStatus.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentID;

    @ManyToOne
    private Client client;

    @ManyToOne
    private BarberService barberservice;

    @ManyToOne
    private Employee employee;

    private LocalDateTime registrationTimestamp;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime modifiedDate;
    private String optionalNotes;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus currentStatus;

    public void changeStatus(AppointmentStatus newStatus) {

        if (this.currentStatus == CANCELADO || this.currentStatus == FINALIZADO)

            throw new InvalidAppointmentUpdateException();

        if (this.currentStatus == REPROGRAMADO && newStatus == PROGRAMADO)

            throw new InvalidAppointmentUpdateException();

        this.currentStatus = newStatus;
    }
}
