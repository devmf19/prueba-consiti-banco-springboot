package com.consiti.serviciofrancisco.service;

import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.entity.Transaction;
import com.consiti.serviciofrancisco.entity.TransactionType;
import com.consiti.serviciofrancisco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public Optional<Transaction> getOne(int id){
        return transactionRepository.findById(id);
    }

    public List<Transaction> findByAccount(Account account){
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> findByTransactionType(TransactionType transactionType){
        return transactionRepository.findByTransactionType(transactionType);
    }
}
