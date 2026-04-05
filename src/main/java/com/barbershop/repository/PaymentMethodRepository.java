package com.barbershop.repository;

import com.barbershop.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndPaymentMethodIDNot(String name, Long paymentMethodID);
}
