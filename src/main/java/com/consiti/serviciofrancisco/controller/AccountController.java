package com.consiti.serviciofrancisco.controller;

import com.consiti.serviciofrancisco.dto.AccountDto;
import com.consiti.serviciofrancisco.dto.Message;
import com.consiti.serviciofrancisco.dto.TransactionDto;
import com.consiti.serviciofrancisco.entity.Account;
import com.consiti.serviciofrancisco.entity.Transaction;
import com.consiti.serviciofrancisco.entity.TransactionType;
import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.service.CustomerService;
import com.consiti.serviciofrancisco.service.AccountService;
import com.consiti.serviciofrancisco.service.SAuthenticatedCustomer;
import com.consiti.serviciofrancisco.service.TransactionService;
import com.consiti.serviciofrancisco.service.TransactionTypeService;
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
    AccountService accountService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionTypeService transactionTypeService;

    @Autowired
    SAuthenticatedCustomer sAuthenticatedCustomer;


    @GetMapping("")
    public ResponseEntity<List<Account>> findAll() {
        List<Account> list = accountService.list();
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
        if (!customerService.existByDui(cuentaDto.getCustomerDui())) {
            return new ResponseEntity<Message>(new Message("No puede asignar una cuenta a un cliente no registrado"), HttpStatus.BAD_REQUEST);
        }
        if (accountService.existByNumber(cuentaDto.getNumber())) {
            return new ResponseEntity<Message>(new Message("Ya existe una cuenta registrada con el número "+cuentaDto.getNumber()), HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        Date date = Date.valueOf(now.format( formatter ));

        Customer customer = customerService.getByDui(cuentaDto.getCustomerDui()).get();

        Account account = new Account();
        account.setNumber(cuentaDto.getNumber());
        account.setName(cuentaDto.getName());
        account.setOpeningAmount(cuentaDto.getOpeningAmount());
        account.setBalance(cuentaDto.getOpeningAmount());
        account.setState('0');
        account.setOpeningDate(date);
        account.setCustomer(customer);

        if(accountService.save(account)){
            return new ResponseEntity<Message>(new Message("Cuenta registrada"), HttpStatus.CREATED);
        }
        return new ResponseEntity<Message>(new Message("Ya tiene una cuenta con el nombre "+cuentaDto.getName()+ ". Use otro nombre"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Message> update(@PathVariable("name") String name, @RequestBody AccountDto accountDto) {
        if (!accountService.existByName(name)) {
            return new ResponseEntity<Message>(new Message("La cuenta no existe"), HttpStatus.BAD_REQUEST);
        }
        if (accountDto.getNumber() == null) {
            return new ResponseEntity<Message>(new Message("El número de cuenta es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (accountDto.getOpeningAmount() < 0 ) {
            return new ResponseEntity<Message>(new Message("El monto de apertura no puede ser inferios a $0"), HttpStatus.BAD_REQUEST);
        }
        if (accountService.existByNumber(accountDto.getNumber())) {
            return new ResponseEntity<Message>(new Message("Ya existe una cuenta registrada con el número "+accountDto.getNumber()), HttpStatus.BAD_REQUEST);
        }
        if (accountService.existByName(accountDto.getName())) {
            return new ResponseEntity<Message>(new Message("Ya existe una cuenta registrada con el nombre"+accountDto.getName()), HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.getByName(name).get(0);
        account.setNumber(accountDto.getNumber());
        account.setName(accountDto.getName());
        account.setBalance(accountDto.getBalance());
        account.setState(accountDto.getState());

        accountService.save(account);
        return new ResponseEntity<Message>(new Message("Cuenta actualizada"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Message> delete(@PathVariable("name") String name) {
        if(!accountService.existByName(name)){
            return new ResponseEntity<Message>(new Message("No se encontró una cuenta con nombre "+name), HttpStatus.NOT_FOUND);
        }
        accountService.deleteByName(name);
        return new ResponseEntity<Message>(new Message("Cuenta "+name+ " eliminada"), HttpStatus.OK);
    }


    @PostMapping("/{name}/transaction")
    public ResponseEntity<Message> newTransaction(@PathVariable("name") String name, @RequestBody TransactionDto transactionDto) {
        if(!accountService.existByName(name)){
            return new ResponseEntity<Message>(new Message("No se encontró una cuenta con nombre "+name), HttpStatus.BAD_REQUEST);
        }
        try {
            Account account = accountService.getByName(name).get(0);

            if(transactionDto.getTransactionType() == 1){

                if(transactionDto.getValue() < 1){
                    return new ResponseEntity<Message>(new Message("El monto mínimo que puede depositar es $1"), HttpStatus.BAD_REQUEST);
                }

            } else if(transactionDto.getTransactionType() == 2){

                if(transactionDto.getValue() > account.getBalance()){
                    return new ResponseEntity<Message>(new Message("No puede retirar más saldo del que tiene. Uested tiene $"+account.getBalance()), HttpStatus.BAD_REQUEST);
                }
                else if(transactionDto.getValue() < 1){
                    return new ResponseEntity<Message>(new Message("El monto mínimo que puede retirar es $1"), HttpStatus.BAD_REQUEST);
                }

            } else {
                return new ResponseEntity<Message>(new Message("Tipo de transacción inválido"), HttpStatus.BAD_REQUEST);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            Date date = Date.valueOf(now.format( formatter ));

            TransactionType transactionType = transactionTypeService.getOne(transactionDto.getTransactionType()).get();

            Transaction transaction = new Transaction();
            transaction.setValue(transactionDto.getValue());
            transaction.setTransactionType(transactionType);
            transaction.setAccount(account);
            transaction.setTransactionDate(date);

            transactionService.save(transaction);

            return new ResponseEntity<Message>(new Message("Transacción hecha"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }


    }


}
