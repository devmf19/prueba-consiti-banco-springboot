package com.consiti.serviciofrancisco.security.repository;

import com.consiti.serviciofrancisco.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByDui(String dui);
    boolean existsByUsername(String username);

    Optional<Customer> findByDui(String dui);
    Optional<Customer> findByUsername(String username);


}
