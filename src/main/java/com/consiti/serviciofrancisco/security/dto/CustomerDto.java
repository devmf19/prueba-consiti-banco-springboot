package com.consiti.serviciofrancisco.security.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class CustomerDto {
    private String dui;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private Date openingDate;
}
