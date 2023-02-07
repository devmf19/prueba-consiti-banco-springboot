package com.consiti.serviciofrancisco.security;

import com.consiti.serviciofrancisco.dto.Message;
import com.consiti.serviciofrancisco.security.dto.CustomerDto;
import com.consiti.serviciofrancisco.security.dto.JwtDto;
import com.consiti.serviciofrancisco.security.dto.LoginDto;
import com.consiti.serviciofrancisco.security.entity.Customer;
import com.consiti.serviciofrancisco.security.jwt.JwtProvider;
import com.consiti.serviciofrancisco.security.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerService userService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("")
    public ResponseEntity<Message> signup (@Valid @RequestBody CustomerDto newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<Message>(new Message("Verifique los datos introducidos"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existByUsername(newUser.getUsername())) {
            return new ResponseEntity<Message>(new Message("El nombre de usuario que ingresó ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existByDui(newUser.getDui())) {
            return new ResponseEntity<Message>(new Message("El dui que ingresó ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        newUser.setOpeningDate(Date.valueOf(now.format( formatter )));

        Customer customer = new Customer();
        customer.setDui(newUser.getDui());
        customer.setName(newUser.getName());
        customer.setLastname(newUser.getLastname());
        customer.setUsername(newUser.getUsername());
        customer.setPassword(passwordEncoder.encode(newUser.getPassword()));
        customer.setOpeningDate(newUser.getOpeningDate());

        userService.save(customer);
        return new ResponseEntity<Message>(new Message("Registro exitoso"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginUser, BindingResult bindingResult){
        log.info("username: "+loginUser.getUsername());
        log.info("password: "+loginUser.getPassword());

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<Message>(new Message("Usuario inválido"), HttpStatus.UNAUTHORIZED);
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        JwtDto token = new JwtDto(jwt);

        Customer customer = userService.getByUsername(loginUser.getUsername()).get();

        HashMap<String, String> response = new HashMap<>();
        response.put("token", token.getToken());
        response.put("dui", customer.getDui());
        response.put("name", customer.getName());
        response.put("lastname", customer.getLastname());
        response.put("username", customer.getUsername());

        return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
    }
}
