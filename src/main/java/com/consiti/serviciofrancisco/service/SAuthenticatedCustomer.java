package com.consiti.serviciofrancisco.service;

import com.consiti.serviciofrancisco.security.repository.IAuthenticatedCustomer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SAuthenticatedCustomer implements IAuthenticatedCustomer {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
