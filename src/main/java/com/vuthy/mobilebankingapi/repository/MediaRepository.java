package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    Optional<Media> findByName(String fileName);
}
