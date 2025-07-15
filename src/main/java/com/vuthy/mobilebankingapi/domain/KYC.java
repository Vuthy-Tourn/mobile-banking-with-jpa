package com.vuthy.mobilebankingapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// KYC: know you customer
@Getter
@Setter
@NoArgsConstructor
@Entity
public class KYC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nationalCardId;

    @Column(nullable = false)
    private Boolean isVerified= false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // associate with foreign key
//    @OneToOne(mappedBy = "kyc")
//    private Customer customer;

    @OneToOne
    @JoinColumn(name = "customer_id")  // Changed from @MapsId
    private Customer customer;
}
