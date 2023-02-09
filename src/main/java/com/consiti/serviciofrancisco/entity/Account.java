package com.consiti.serviciofrancisco.entity;

import com.consiti.serviciofrancisco.security.entity.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    @NotNull
    @Column(unique = true)
    private String number;
    @NotNull
    private String name;
    @NotNull
    @Min(0)
    private double openingAmount;
    @NotNull
    private Date openingDate;
    @NotNull
    @Min(0)
    private double balance;
    @NotNull
    private char state;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private Customer customer;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
