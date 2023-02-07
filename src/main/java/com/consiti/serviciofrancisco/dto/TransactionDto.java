package com.consiti.serviciofrancisco.dto;

import com.consiti.serviciofrancisco.entity.TransactionType;
import lombok.Data;

import javax.validation.constraints.Min;
import java.sql.Date;
@Data
public class TransactionDto {
    private int transactionId;
    private String accountId;
    @Min(1)
    private double value;
    private Date transactionDate;
    private String transactionType;
}
