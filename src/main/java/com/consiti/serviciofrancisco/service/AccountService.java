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
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    SAuthenticatedCustomer sAuthenticatedCustomer;

    public List<Account> list(){
        String username = sAuthenticatedCustomer.getAuthentication().getName();
        Customer customer = customerService.getByUsername(username).get();
        List<Account> accounts = accountRepository.findByCustomer(customer);
        return accounts;
    }

    public List<Account> getByName(String name){
        List<Account> customerAccounts = list();
        customerAccounts = customerAccounts.stream()
                .filter(account -> account.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        return  customerAccounts;
    }

    public boolean existByName(String name){
        List<Account> customerAccount = getByName(name);

        if(!customerAccount.isEmpty()){
            return true;
        }
        return  false;
    }

    public void deleteByName(String name){
        List<Account> customerAccount = getByName(name);

        if(!customerAccount.isEmpty()){
            int id = customerAccount.get(0).getAccountId();
            accountRepository.deleteById(id);
        }
    }

    public boolean save(Account newAccount){
        List<Account> customerAccount = getByName(newAccount.getName());

        if(customerAccount.isEmpty()){
            accountRepository.save(newAccount);
            return true;
        }
        return false;
    }

    public boolean existByNumber(String number){
        return accountRepository.existsByNumber(number);
    }




}
