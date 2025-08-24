package com.bankwebapp.controllers;


import com.bankwebapp.dtos.AccountDto;
import com.bankwebapp.dtos.CreateAccountRequest;
import com.bankwebapp.dtos.UpdateAccountRequest;
import com.bankwebapp.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }
    @PutMapping ("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id, @RequestBody UpdateAccountRequest updateAccountRequest) {
        return ResponseEntity.ok(accountService.updateAccount(id, updateAccountRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccountById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping ("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withdrawMoney(@PathVariable String id, @PathVariable Double amount) {
        return ResponseEntity.ok(accountService.withdrawMoney(id, amount));

    }

    @PutMapping ("/deposit/{id}/{amount}")
    public ResponseEntity<AccountDto> depositMoney(@PathVariable String id, @PathVariable Double amount) {
        return ResponseEntity.ok(accountService.depositMoney(id, amount));
    }

}
