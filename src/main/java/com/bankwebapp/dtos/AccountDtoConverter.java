package com.bankwebapp.dtos;


import com.bankwebapp.models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {

    public AccountDto convert(Account account){

        return AccountDto.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();

    }


}
