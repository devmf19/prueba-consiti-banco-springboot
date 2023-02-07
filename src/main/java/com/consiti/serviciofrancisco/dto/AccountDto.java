package com.consiti.serviciofrancisco.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.sql.Date;
@Data
public class AccountDto {
    private int accountId;
    private String number;
    @Min(0)
    private double openingAmount;
    private Date openingDate;
    @Min(0)
    private double balance;
    private char state;
    private int customerId;
}
