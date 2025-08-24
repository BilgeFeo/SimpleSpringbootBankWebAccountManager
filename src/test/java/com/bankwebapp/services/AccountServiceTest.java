package com.bankwebapp.services;

import com.bankwebapp.dtos.*;
import com.bankwebapp.models.Account;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Currency;
import com.bankwebapp.repositories.AccountRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {



    @InjectMocks
    private AccountService accountService;
    @Mock
    private  AccountRepository accountRepository;
    @Mock
    private  CustomerService customerService;
    @Mock
    private AccountDtoConverter accountDtoConverter;




    @Test
    public void whenCreateAccountCalledWithValidRequest_itShouldReturnValidAccountDto() {
        CreateAccountRequest createAccountRequest  = new CreateAccountRequest();
        createAccountRequest.setId("123");
        createAccountRequest.setBalance(1000.0);
        createAccountRequest.setCurrency(Currency.USD);
        createAccountRequest.setCustomerId("456");
        createAccountRequest.setCity(City.ANKARA);


        CustomerDto customer = CustomerDto.builder()
                .id("456")
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1998)
                .city(CityDto.ANKARA)
                .build();

                Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();
        AccountDto accountDto = AccountDto.builder()
                .id("123")
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("456").build();


        Mockito.when(customerService.getCustomerById("456")).thenReturn(customer);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.when(accountDtoConverter.convert(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(createAccountRequest);

        Assert.assertEquals(accountDto, result);
        Mockito.verify(customerService).getCustomerById("456");
        Mockito.verify(accountRepository).save(account);
        Mockito.verify(accountDtoConverter).convert(account);
    }


    @Test
    public void whenCreateAccountCalledWithNonExistCustomer_itShouldReturnEmtryAccountDto() {

        CreateAccountRequest createAccountRequest  = new CreateAccountRequest();
        createAccountRequest.setId("123");
        createAccountRequest.setBalance(1000.0);
        createAccountRequest.setCurrency(Currency.USD);
        createAccountRequest.setCustomerId("456");
        createAccountRequest.setCity(City.ANKARA);


        Mockito.when(customerService.getCustomerById("456")).thenReturn(CustomerDto.builder().build());
        AccountDto expectedAccountDto = AccountDto.builder().build();
        AccountDto result = accountService.createAccount(createAccountRequest);
        Assert.assertEquals(result,expectedAccountDto);
        Mockito.verifyNoInteractions(accountRepository);
        Mockito.verifyNoInteractions(accountDtoConverter);




    }

    //simplicity is complicated, complexity is simple
    /*
    * MasterBranch ana branchdir. Future Branchler Buradan Ayrilir Ve sonrasinda tekrar buraya merge edilir.
    * Feature Branchler yeni özellikler eklemek için kullanılır. Master Branch'ten ayrılır ve geliştirme tamamlandığında tekrar Master Branch'e merge edilir.
    * Development Branch, Hata durumu dosya icerik degisimi tarzi bir degisimdir. Master Branch'ten ayrılır ve geliştirme tamamlandığında tekrar Master Branch'e merge edilir.
    * Release Master Branchden cikarilir.
    * Rollback islemleri yapilabilir.
    * Hotfix Branch, acil durumlarda kullanılır. Master Branch'ten ayrılır ve acil düzeltmeler yapıldıktan sonra tekrar Master Branch'e merge edilir.
    * Gitte uc adet durum vardir. timeline uzak bir sunucuda bulunur. Origin uzak sunucudur.
    * git pull ile uzak sunucudan local e kod dosyalari cekilir.
    *
    *
    *
    *
    * */




    @Test (expected = RuntimeException.class)
    public void whenCreateAccountCalledAndRepositoryThrowsException_itShouldThrowException() {

        CreateAccountRequest createAccountRequest  = new CreateAccountRequest();
        createAccountRequest.setId("123");
        createAccountRequest.setBalance(1000.0);
        createAccountRequest.setCurrency(Currency.USD);
        createAccountRequest.setCustomerId("456");
        createAccountRequest.setCity(City.ANKARA);


        CustomerDto customer = CustomerDto.builder()
                .id("456")
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1998)
                .city(CityDto.ANKARA)
                .build();

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();
        AccountDto accountDto = AccountDto.builder()
                .id("123")
                .balance(1000.0) 
                .currency(Currency.USD)
                .customerId("456").build();


        Mockito.when(customerService.getCustomerById("456")).thenReturn(customer);
        Mockito.when(accountRepository.save(account)).thenThrow(new RuntimeException("Pronlem with saving account :)"));


        AccountDto result = accountService.createAccount(createAccountRequest);

        Assert.assertEquals(accountDto, result);
        Mockito.verify(customerService).getCustomerById("456");
        Mockito.verify(accountRepository).save(account);
        Mockito.verify(accountDtoConverter).convert(account);


    }
}