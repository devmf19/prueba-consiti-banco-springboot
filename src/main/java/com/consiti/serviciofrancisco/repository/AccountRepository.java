package com.consiti.serviciofrancisco.repository;

import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,  Integer> {
    boolean existsByNumber(String number);
    boolean existsByName(String name);

    Optional<Account> findByNumber(String number);
    Optional<Account> findByName(String name);
    List<Account> findByCustomer(Customer customer);

    void deleteByAccountId(int id);
    void deleteByNumber(String number);
    void deleteByName(String name);
}
