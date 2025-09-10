package com.bankwebapp.services;


import com.bankwebapp.dtos.CreateCustomerRequest;
import com.bankwebapp.dtos.CustomerDto;
import com.bankwebapp.dtos.CustomerDtoConverter;
import com.bankwebapp.dtos.UpdateCustomerRequest;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Customer;
import com.bankwebapp.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter customerDtoConverter;
    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter customerDtoConverter) {
        this.customerRepository = customerRepository;
        this.customerDtoConverter = customerDtoConverter;
    }


    public CustomerDto createCustomer (CreateCustomerRequest customerRequest){

        Customer customer = new Customer();
        customer.setId(customerRequest.getId());
        customer.setName(customerRequest.getName());
        customer.setAddres(customerRequest.getAddres());
        customer.setDateofBirth(customerRequest.getDateofBirth());
        customer.setCity(City.valueOf(customerRequest.getCity().name()));

        Customer savedCustomer = customerRepository.save(customer);

        return customerDtoConverter.getCustomerDto(savedCustomer);
    }

    public List<CustomerDto> getAllCustomers(){

        List<Customer> customers = customerRepository.findAll();
        List <CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer : customers){
            customerDtos.add(customerDtoConverter.getCustomerDto(customer));
        }
        return customerDtos;
    }

    public CustomerDto getCustomerById(String id){

        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.map( customerDtoConverter::getCustomerDto).orElse(new CustomerDto());

    }

    public void deleteCustomerById(String id) {
        customerRepository.deleteById(id);

    }

    public CustomerDto updateCustomer(String id, UpdateCustomerRequest updateCustomerRequest) {


        Optional<Customer> customerOptional = customerRepository.findById(id);

        customerOptional.ifPresent(customer -> {customer.setName( updateCustomerRequest.getName() );
            customer.setCity(City.valueOf(updateCustomerRequest.getCity().name()));
            customer.setAddres( updateCustomerRequest.getAddres() );
            customer.setDateofBirth(updateCustomerRequest.getDateofBirth());
            customerRepository.save(customer);
        });


        return customerOptional.map( customerDtoConverter::getCustomerDto).orElse(new CustomerDto());
    }

    protected Customer getCustomerDtoById  (String id){

        return customerRepository.findById(id).orElse(new Customer());

    }










}
