package com.bankwebapp.controllers;


import com.bankwebapp.dtos.CreateCustomerRequest;
import com.bankwebapp.dtos.CustomerDto;
import com.bankwebapp.dtos.UpdateCustomerRequest;
import com.bankwebapp.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/customer")
public class CustomesController {

    private final CustomerService customerService;

    public CustomesController(CustomerService customerSercive) {
        this.customerService = customerSercive;
    }


    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
    return ResponseEntity.ok("Hello World");
    }


    @PostMapping("/post")
    public ResponseEntity <CustomerDto> createCustomer(@RequestBody CreateCustomerRequest customerRequest){


    return ResponseEntity.ok(customerService.createCustomer(customerRequest));
    }

    @GetMapping
    public ResponseEntity <List<CustomerDto>> getAllCustomers(){
        return ResponseEntity.ok( customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity <CustomerDto> getCustomer(@PathVariable String id){


        return ResponseEntity.ok(customerService.getCustomerById(id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable String id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping ("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest, @PathVariable String id){



        return ResponseEntity.ok(customerService.updateCustomer(id,updateCustomerRequest));
    }
}
