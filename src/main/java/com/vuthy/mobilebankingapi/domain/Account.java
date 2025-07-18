package com.vuthy.mobilebankingapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, unique = true, nullable = false)
    private String actNo;

    @Column(nullable = false, length = 50)
    private String actName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "actType_id")
    private AccountType actType;

    @Column(length = 30, nullable = false)
    private String actCurrency;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Boolean isHide;

    @Column(nullable = false)
    private BigDecimal overLimit;

    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> senderTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receiverTransactions;
}
