package com.consiti.serviciofrancisco.controller;

import com.consiti.serviciofrancisco.dto.AccountDto;
import com.consiti.serviciofrancisco.dto.Message;
import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.service.CustomerService;
import com.consiti.serviciofrancisco.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    AccountService cuentaService;

    @Autowired
    CustomerService clienteService;

    @GetMapping("")
    public ResponseEntity<List<Account>> findAll() {
        List<Account> list = cuentaService.list();
        return new ResponseEntity<List<Account>>(list, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Message> create(@RequestBody AccountDto cuentaDto) {
        if (cuentaDto.getNumber() == null) {
            return new ResponseEntity<Message>(new Message("El número de cuenta es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (cuentaDto.getOpeningAmount() < 0 ) {
            return new ResponseEntity<Message>(new Message("El monto de apertura no puede ser inferios a $0"), HttpStatus.BAD_REQUEST);
        }
        if (!clienteService.existByDui(cuentaDto.getCustomerDui())) {
            return new ResponseEntity<Message>(new Message("No se puede asignar una cuenta a un cliente no registrado"), HttpStatus.BAD_REQUEST);
        }
        if (cuentaService.existByNumber(cuentaDto.getNumber())) {
            return new ResponseEntity<Message>(new Message("Ya existe una cuenta registrada con el número "+cuentaDto.getNumber()), HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        Date date = Date.valueOf(now.format( formatter ));

        Customer customer = clienteService.getByDui(cuentaDto.getCustomerDui()).get();

        Account account = new Account();
        account.setNumber(cuentaDto.getNumber());
        account.setName(cuentaDto.getName());
        account.setOpeningAmount(cuentaDto.getOpeningAmount());
        account.setBalance(cuentaDto.getOpeningAmount());
        account.setState('0');
        account.setOpeningDate(date);
        account.setCustomer(customer);

        cuentaService.save(account);
        return new ResponseEntity<Message>(new Message("Cuenta registrada"), HttpStatus.CREATED);
    }
}
