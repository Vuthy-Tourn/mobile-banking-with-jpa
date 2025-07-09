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
    private Integer id;

    @Column(nullable = false)
    private String nationalCardId;

    @Column(nullable = false)
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean isDeleted;

    // associate with foreign key
//    @OneToOne(mappedBy = "kyc")
//    private Customer customer;

    @OneToOne
    @MapsId
    @JoinColumn(name = "cust_id")
    private Customer customer;
}
