package com.consiti.serviciofrancisco.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class PrincipalCustomer implements UserDetails {
    private String dui;
    private String name;
    private String lastname;
    private String username;
    private String password;
    //private Date openingDate;
    protected Collection<? extends GrantedAuthority> authorities;

    public PrincipalCustomer(String dui, String name, String lastname, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.dui = dui;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    private static Set<String> customerRole = new HashSet<String>(){{
        add("ROLE_USER");
    }};

    public static PrincipalCustomer build(Customer c){
        List<GrantedAuthority> authorities1 = customerRole
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        return new PrincipalCustomer(c.getDui(), c.getName(), c.getLastname(), c.getUsername(),  c.getPassword(), authorities1);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
