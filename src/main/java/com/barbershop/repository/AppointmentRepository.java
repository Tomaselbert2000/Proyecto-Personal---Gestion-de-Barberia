package com.barbershop.repository;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(
            "SELECT " +
                    "COUNT(A) > 0 " +
                    "FROM Appointment A " +
                    "WHERE " +
                    "A.employee.employeeID=:employeeID " +
                    "AND (:startDateTime < A.endDateTime AND :endDateTime > A.startDateTime)"
    )
    boolean existsOverlappingAppointmentOnCreate(
            @Param("employeeID") Long employeeID,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query(
            "SELECT " +
                    "COUNT(A) > 0 " +
                    "FROM Appointment A " +
                    "WHERE " +
                    "A.employee.employeeID=:employeeID " +
                    "AND (:startDateTime < A.endDateTime AND :endDateTime > A.startDateTime)" +
                    "AND A.appointmentID <> :appointmentIDToExclude"
    )
    boolean existsOverlappingAppointmentOnUpdate(
            @Param("employeeID") Long employeeID,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("appointmentIDToExclude") Long appointmentIDToExclude
    );

    Long countByStartDateTimeBetween(LocalDateTime startDateTimeAfter, LocalDateTime startDateTimeBefore);

    Long countByStartDateTimeBetweenAndCurrentStatus(LocalDateTime startDateTime, LocalDateTime startDateTime2, AppointmentStatus currentStatus);

    Long countByCurrentStatus(AppointmentStatus currentStatus);

    Long countByRegistrationTimestampAfter(LocalDateTime registrationTimestampAfter);

    Long countByRegistrationTimestampBetween(LocalDateTime registrationTimestampAfter, LocalDateTime registrationTimestampBefore);

    Long countByCurrentStatusAndRegistrationTimestampBetween(AppointmentStatus currentStatus, LocalDateTime registrationTimestampAfter, LocalDateTime registrationTimestampBefore);

    List<Appointment> findTop5ByOrderByRegistrationTimestampDesc();

    List<Appointment> findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus currentStatus);
}
