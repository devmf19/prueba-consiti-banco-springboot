package com.consiti.serviciofrancisco.dto;

import lombok.Data;

import javax.validation.constraints.Min;
@Data
public class AccountDto {
    private String number;
    String name;
    @Min(0)
    private double openingAmount;
    private String customerDui;
}
