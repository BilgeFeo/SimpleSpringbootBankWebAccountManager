package com.bankwebapp.dtos;


import com.bankwebapp.models.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoConverter {

    public CustomerDto getCustomerDto(Customer customer){

        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .addres(customer.getAddres())
                .dateofBirth(customer.getDateofBirth())
                .city(CityDto.valueOf(customer.getCity().name()))
                .build();
    }





}
