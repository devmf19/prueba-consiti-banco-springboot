package com.consiti.serviciofrancisco.repository;

import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.entity.Transaction;
import com.consiti.serviciofrancisco.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccount(Account account);

    List<Transaction> findByTransactionType(TransactionType transactionType);
}
