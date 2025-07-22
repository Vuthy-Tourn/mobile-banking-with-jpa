package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
