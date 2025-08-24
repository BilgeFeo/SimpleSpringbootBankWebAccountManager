package com.bankwebapp.services;


import com.bankwebapp.dtos.*;

import com.bankwebapp.models.Account;
import com.bankwebapp.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter accountDtoConverter;

    public AccountService(AccountRepository accountRepository, CustomerService customerService, AccountDtoConverter accountDtoConverter) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.accountDtoConverter = accountDtoConverter;
    }



    public AccountDto createAccount (CreateAccountRequest createAccountRequest){
        CustomerDto customer =customerService.getCustomerById(createAccountRequest.getCustomerId());
        if(customer.getId() == null||customer.getId().equals("")){
            return AccountDto.builder().build();
        }
        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();



        return accountDtoConverter.convert(accountRepository.save(account));
    }

    public AccountDto updateAccount (String id, UpdateAccountRequest updateAccountRequest){
        CustomerDto customer = customerService.getCustomerById(updateAccountRequest.getCustomerId());

        if(customer.getId() == null){
            return AccountDto.builder().build();}
        Optional <Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            account.setBalance(updateAccountRequest.getBalance());
            account.setCity(updateAccountRequest.getCity());
            account.setCurrency(updateAccountRequest.getCurrency());
            account.setCustomerId(updateAccountRequest.getCustomerId());

            accountRepository.save(account);
        });
        return accountOptional.map(accountDtoConverter::convert).orElse(AccountDto.builder().build());



    }

    public List<AccountDto> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(accountDtoConverter::convert).collect(Collectors.toList());



    }

    public AccountDto getAccountById(String id){

        return accountRepository.findById(id)
                .map(accountDtoConverter::convert)
                .orElse(AccountDto.builder().build());
    }

    public void deleteAccountById(String id) {
        accountRepository.deleteById(id);
    }


    public AccountDto withdrawMoney(String id, double amount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
                return accountDtoConverter.convert(account);
            } else {
                // Handle insufficient funds
                return AccountDto.builder().build();
            }
        }
        return AccountDto.builder().build();

    }

    public AccountDto depositMoney(String id, double amount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
            return accountDtoConverter.convert(account);
        }
        return AccountDto.builder().build();
    }







}
