package com.barbershop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientID;
    @Column(unique = true, nullable = false)
    private String nationalIdentityCardNumber;
    private String firstName;
    private String lastName;
    @Column(nullable = false, updatable = false)
    private LocalDate registrationDate;
    @Column(unique = true, nullable = false)
    private String email;
    @ElementCollection
    @CollectionTable(name = "client_phones", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "phone")
    private List<String> phoneNumbersList;
    private String optionalNotes;
}
