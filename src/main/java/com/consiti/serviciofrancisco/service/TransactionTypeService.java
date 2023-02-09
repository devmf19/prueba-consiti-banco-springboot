package com.consiti.serviciofrancisco.service;

import com.consiti.serviciofrancisco.entity.TransactionType;
import com.consiti.serviciofrancisco.repository.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionTypeService {
    @Autowired
    TransactionTypeRepository transactionTypeRepository;

    public void save(TransactionType transactionType) {
        transactionTypeRepository.save(transactionType);
    }

    public Optional<TransactionType> getOne(int id){
        return transactionTypeRepository.findByTtId(id);
    }
    public Optional<TransactionType> findByName(String name) {
        return  transactionTypeRepository.findByName(name);
    }
}
