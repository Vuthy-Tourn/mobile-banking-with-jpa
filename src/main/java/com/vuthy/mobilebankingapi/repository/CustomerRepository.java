package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    //JPQL
    @Modifying
    @Query(value = """
    UPDATE Customer c set c.isDeleted = TRUE
    WHERE c.phoneNumber = :phoneNumber
""")
    void disableByPhoneNumber(String phoneNumber);


    @Query(value = """
    SELECT EXISTS ( SELECT c from Customer c WHERE c.phoneNumber = ?1)
""")
    Boolean isExistsByPhoneNumber(String phoneNumber);



    // Derived method
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);
    List<Customer> findAllByIsDeletedFalse();
}
