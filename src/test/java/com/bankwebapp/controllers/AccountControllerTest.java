package com.bankwebapp.controllers;

import com.bankwebapp.dtos.*;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Currency;
import com.bankwebapp.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAccountsCalled_itShouldReturnAccountList() throws Exception {
        AccountDto account1 = AccountDto.builder()
                .id("1")
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("customer1")
                .build();
        
        AccountDto account2 = AccountDto.builder()
                .id("2")
                .balance(2000.0)
                .currency(Currency.EUR)
                .customerId("customer2")
                .build();

        List<AccountDto> accounts = Arrays.asList(account1, account2);

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].balance").value(2000.0))
                .andExpect(jsonPath("$[1].currency").value("EUR"));

        verify(accountService).getAllAccounts();
    }

    @Test
    void whenGetAccountByIdCalled_itShouldReturnAccount() throws Exception {
        String accountId = "123";
        AccountDto account = AccountDto.builder()
                .id(accountId)
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("customer1")
                .build();

        when(accountService.getAccountById(accountId)).thenReturn(account);

        mockMvc.perform(get("/account/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.customerId").value("customer1"));

        verify(accountService).getAccountById(accountId);
    }

    @Test
    void whenCreateAccountCalled_itShouldCreateAndReturnAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setId("123");
        request.setBalance(1000.0);
        request.setCurrency(Currency.USD);
        request.setCustomerId("customer1");
        request.setCity(City.ANKARA);

        AccountDto expectedAccount = AccountDto.builder()
                .id("123")
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("customer1")
                .build();

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(expectedAccount);

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.customerId").value("customer1"));

        verify(accountService).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    void whenUpdateAccountCalled_itShouldUpdateAndReturnAccount() throws Exception {
        String accountId = "123";
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setBalance(2000.0);
        request.setCurrency(Currency.EUR);
        request.setCustomerId("customer1");
        request.setCity(City.ISTANBUL);

        AccountDto expectedAccount = AccountDto.builder()
                .id(accountId)
                .balance(2000.0)
                .currency(Currency.EUR)
                .customerId("customer1")
                .build();

        when(accountService.updateAccount(eq(accountId), any(UpdateAccountRequest.class))).thenReturn(expectedAccount);

        mockMvc.perform(put("/account/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.balance").value(2000.0))
                .andExpect(jsonPath("$.currency").value("EUR"));

        verify(accountService).updateAccount(eq(accountId), any(UpdateAccountRequest.class));
    }

    @Test
    void whenDeleteAccountCalled_itShouldDeleteAccount() throws Exception {
        String accountId = "123";

        mockMvc.perform(delete("/account/{id}", accountId))
                .andExpect(status().isOk());

        verify(accountService).deleteAccountById(accountId);
    }

    @Test
    void whenWithdrawMoneyCalled_itShouldReturnUpdatedAccount() throws Exception {
        String accountId = "123";
        Double amount = 500.0;
        
        AccountDto expectedAccount = AccountDto.builder()
                .id(accountId)
                .balance(500.0)
                .currency(Currency.USD)
                .customerId("customer1")
                .build();

        when(accountService.withdrawMoney(accountId, amount)).thenReturn(expectedAccount);

        mockMvc.perform(put("/account/withdraw/{id}/{amount}", accountId, amount))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.balance").value(500.0));

        verify(accountService).withdrawMoney(accountId, amount);
    }

    @Test
    void whenDepositMoneyCalled_itShouldReturnUpdatedAccount() throws Exception {
        String accountId = "123";
        Double amount = 500.0;
        
        AccountDto expectedAccount = AccountDto.builder()
                .id(accountId)
                .balance(1500.0)
                .currency(Currency.USD)
                .customerId("customer1")
                .build();

        when(accountService.depositMoney(accountId, amount)).thenReturn(expectedAccount);

        mockMvc.perform(put("/account/deposit/{id}/{amount}", accountId, amount))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.balance").value(1500.0));

        verify(accountService).depositMoney(accountId, amount);
    }
}