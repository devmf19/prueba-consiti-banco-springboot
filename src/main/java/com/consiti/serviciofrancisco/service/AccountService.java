package com.consiti.serviciofrancisco.service;

import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.repository.AccountRepository;
import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    SAuthenticatedCustomer sAuthenticatedCustomer;

    public void save(Account account){
        accountRepository.save(account);
    }

    public boolean existById(int id){
        return accountRepository.existsById(id);
    }

    public boolean existByNumber(String number){
        return accountRepository.existsByNumber(number);
    }

    public boolean existByName(String name){
        return accountRepository.existsByName(name);
    }

    public Optional<Account> getOne(int id){
        return accountRepository.findById(id);
    }

    public Optional<Account> getByNumber(String number){
        return  accountRepository.findByNumber(number);
    }

    public Optional<Account> getByName(String name){
        return  accountRepository.findByName(name);
    }

    public List<Account> list(){
        String username = sAuthenticatedCustomer.getAuthentication().getName();
        Customer customer = customerService.getByUsername(username).get();
        return accountRepository.findByCustomer(customer);
    }

    public void delete(int id){
        accountRepository.deleteById(id);
    }

    public void deleteByName(String name){
        accountRepository.deleteByName(name);
    }

    public void deleteByNumber(String number){
        accountRepository.deleteByNumber(number);
    }




}
