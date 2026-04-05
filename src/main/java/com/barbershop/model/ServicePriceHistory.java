package com.barbershop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ServicePriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceHistoryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_service_id")
    private BarberService barberService;

    private Double priceAtMoment;
    private LocalDateTime timestamp;
}
