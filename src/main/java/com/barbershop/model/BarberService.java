package com.barbershop.model;

import com.barbershop.enums.BarberServiceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BarberService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barbershopServiceID;
    private String name;
    private Double price;
    private BarberServiceCategory serviceCategory;
    private LocalDateTime registrationTimestamp;
    private LocalDateTime modifiedDate;
    private String internalNotes;

    @OneToMany(mappedBy = "barberService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicePriceHistory> servicePriceHistoryList;
}
