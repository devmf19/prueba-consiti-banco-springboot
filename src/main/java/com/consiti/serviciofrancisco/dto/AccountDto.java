package com.consiti.serviciofrancisco.dto;

import lombok.Data;

import javax.validation.constraints.Min;
@Data
public class AccountDto {
    private String number;
    String name;
    @Min(0)
    private double openingAmount;
    @Min(0)
    private double balance;
    private String customerDui;
    private char state;
}
