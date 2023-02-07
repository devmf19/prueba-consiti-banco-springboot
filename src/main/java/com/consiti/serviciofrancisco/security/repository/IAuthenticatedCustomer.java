package com.consiti.serviciofrancisco.security.repository;

import org.springframework.security.core.Authentication;

public interface IAuthenticatedCustomer {
    Authentication getAuthentication();
}
