package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.KYC;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KYCRepository extends CrudRepository<KYC, Integer> {
    Boolean existsByNationalCardId(String nationalCardId);
    Optional<KYC> findByNationalCardId(String nationalCardId);
}
