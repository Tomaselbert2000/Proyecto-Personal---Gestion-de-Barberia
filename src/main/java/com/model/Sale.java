package com.model;

import com.enums.SaleCompositionFilter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleID;

    private LocalDateTime dateAndTime;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "barber_service_id")
    private BarberService barberService;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethodUsed;

    @OneToOne(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private ServiceRecord serviceRecord;

    private Double total;
    private Double modifierValue;

    @Enumerated(EnumType.STRING)
    private SaleCompositionFilter saleComposition;

    @PrePersist
    private void setSaleCompositionType() {

        if (barberService != null && !items.isEmpty()) this.saleComposition = SaleCompositionFilter.VENTA_MIXTA;

        if (barberService != null && items.isEmpty()) this.saleComposition = SaleCompositionFilter.SOLO_SERVICIO;

        if (barberService == null && !items.isEmpty()) this.saleComposition = SaleCompositionFilter.SOLO_PRODUCTOS;
    }
}