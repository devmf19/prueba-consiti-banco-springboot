package com.consiti.serviciofrancisco.security.service;

import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public void save(Customer customer){
        customerRepository.save(customer);
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getByUsername(String username){
        return customerRepository.findByUsername(username);
    }

    public Optional<Customer> getByDui(String dui){
        return customerRepository.findByDui(dui);
    }

    public boolean existByUsername(String username){
        return customerRepository.existsByUsername(username);
    }

    public boolean existByDui(String dui){
        return customerRepository.existsByDui(dui);
    }
}
