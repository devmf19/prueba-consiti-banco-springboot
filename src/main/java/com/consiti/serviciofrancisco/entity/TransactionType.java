package com.consiti.serviciofrancisco.entity;

import com.consiti.serviciofrancisco.enums.TransactionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ttId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionName name;
}
