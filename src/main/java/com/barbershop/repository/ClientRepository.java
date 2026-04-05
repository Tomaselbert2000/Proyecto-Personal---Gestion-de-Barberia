package com.barbershop.repository;

import com.barbershop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    boolean existsByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(c) > 0 FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones")
    boolean existsByAnyPhoneNumberInList(@Param("phones") List<String> phones);

    boolean existsByNationalIdentityCardNumberAndClientIDNot(String nationalIdentityCardNumber, Long clientID);

    boolean existsByEmailAndClientIDNot(String email, Long clientID);

    @Query("SELECT COUNT(c) > 0 FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones AND c.clientID != :idExclude")
    boolean existsByAnyPhoneNumberInListAndClientIDNot(@Param("phones") List<String> phones, @Param("idExclude") Long clientID);

    Long countByRegistrationDateBetween(LocalDate registrationDateAfter, LocalDate registrationDateBefore);

    List<Client> findTop5ByOrderByRegistrationDateDesc();
}
