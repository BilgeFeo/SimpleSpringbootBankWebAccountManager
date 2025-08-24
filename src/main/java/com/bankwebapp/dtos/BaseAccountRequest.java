package com.bankwebapp.dtos;


import com.bankwebapp.models.City;
import com.bankwebapp.models.Currency;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BaseAccountRequest {

    private String id;
    private String customerId;
    private double balance;
    private City city;
    private Currency currency;

}
