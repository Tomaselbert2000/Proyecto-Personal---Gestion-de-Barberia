package com.repository;

import com.enums.PaymentMethodModifierType;
import com.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndPaymentMethodIDNot(String name, Long paymentMethodID);

    @Query("SELECT pm FROM PaymentMethod pm WHERE LOWER(pm.name) LIKE LOWER(CONCAT('%', :paymentName, '%')) AND (pm.isActive = :isActive OR :isActive IS NULL) AND (pm.modifierType = :modifierType OR :modifierType IS NULL)")
    List<PaymentMethod> paymentMethodLiveSearch(
            @Param("paymentName") String paymentName,
            @Param("isActive") Boolean isActive,
            @Param("modifierType") PaymentMethodModifierType modifierType
    );

    @Query("SELECT COUNT(*) FROM PaymentMethod WHERE isActive = TRUE")
    Long getCountMarkedAsActive();

    @Query("SELECT COUNT(*) FROM PaymentMethod WHERE isActive = FALSE")
    Long getCountMarkedAsInactive();

    @Query("SELECT COUNT(*) FROM PaymentMethod WHERE modifierType != :modifierType")
    Long countByModifierType(@Param("modifierType") PaymentMethodModifierType modifierType);

    PaymentMethod findPaymentMethodByName(String name);
}