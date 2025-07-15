package com.vuthy.mobilebankingapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "segments")
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private BigDecimal overLimit;

    @OneToMany(mappedBy = "segment")
    private List<Customer> customers;

}
