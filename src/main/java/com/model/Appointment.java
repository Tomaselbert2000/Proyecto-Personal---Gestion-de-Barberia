package com.model;

import com.enums.AppointmentStatus;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.enums.AppointmentStatus.*;

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
    private String guestFirstName;
    private String guestLastName;
    private String guestPhone;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus currentStatus;

    public void changeStatus(AppointmentStatus newStatus) {

        if (this.currentStatus == CANCELADO || this.currentStatus == FINALIZADO)

            throw new InvalidAppointmentUpdateException();

        if (this.currentStatus == REPROGRAMADO && newStatus == PROGRAMADO)

            throw new InvalidAppointmentUpdateException();

        this.currentStatus = newStatus;
    }

    @PrePersist
    private void setDefaultValues() {

        this.registrationTimestamp = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
        this.currentStatus = PROGRAMADO;
    }
}
