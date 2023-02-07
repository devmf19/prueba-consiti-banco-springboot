package com.consiti.serviciofrancisco.security.service;

import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.entity.PrincipalCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {
    @Autowired
    CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.getByUsername(username).get();
        return PrincipalCustomer.build(customer);
    }
}
