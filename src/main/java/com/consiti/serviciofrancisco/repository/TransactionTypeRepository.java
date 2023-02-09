package com.consiti.serviciofrancisco.repository;

import com.consiti.serviciofrancisco.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
    Optional<TransactionType> findByName(String name);

    Optional<TransactionType> findByTtId(int id);

}
