package com.bankwebapp.services;

import com.bankwebapp.dtos.*;
import com.bankwebapp.models.Account;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Currency;
import com.bankwebapp.repositories.AccountRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {



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


        when(customerService.getCustomerById("456")).thenReturn(customer);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountDtoConverter.convert(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(createAccountRequest);

        assertEquals(accountDto, result);
        verify(customerService).getCustomerById("456");
        verify(accountRepository).save(account);
        verify(accountDtoConverter).convert(account);
    }


    @Test
    public void whenCreateAccountCalledWithNonExistCustomer_itShouldReturnEmtryAccountDto() {

        CreateAccountRequest createAccountRequest  = new CreateAccountRequest();
        createAccountRequest.setId("123");
        createAccountRequest.setBalance(1000.0);
        createAccountRequest.setCurrency(Currency.USD);
        createAccountRequest.setCustomerId("456");
        createAccountRequest.setCity(City.ANKARA);


        when(customerService.getCustomerById("456")).thenReturn(CustomerDto.builder().build());
        AccountDto expectedAccountDto = AccountDto.builder().build();
        AccountDto result = accountService.createAccount(createAccountRequest);
        assertEquals(result,expectedAccountDto);
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(accountDtoConverter);




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




    @Test
    void whenCreateAccountCalledAndRepositoryThrowsException_itShouldThrowException() {

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


        when(customerService.getCustomerById("456")).thenReturn(customer);
        when(accountRepository.save(account)).thenThrow(new RuntimeException("Problem with saving account :)"));

        assertThrows(RuntimeException.class, () -> {
            accountService.createAccount(createAccountRequest);
        });

        verify(customerService).getCustomerById("456");
        verify(accountRepository).save(account);
    }

    @Test
    void whenUpdateAccountCalledWithValidRequest_itShouldReturnUpdatedAccountDto() {
        String accountId = "123";
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setCustomerId("456");
        updateRequest.setBalance(2000.0);
        updateRequest.setCurrency(Currency.EUR);
        updateRequest.setCity(City.ISTANBUL);

        CustomerDto customer = CustomerDto.builder()
                .id("456")
                .name("John Doe")
                .build();

        Account existingAccount = Account.builder()
                .id(accountId)
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("456")
                .city(City.ANKARA)
                .build();

        Account updatedAccount = Account.builder()
                .id(accountId)
                .balance(2000.0)
                .currency(Currency.EUR)
                .customerId("456")
                .city(City.ISTANBUL)
                .build();

        AccountDto expectedDto = AccountDto.builder()
                .id(accountId)
                .balance(2000.0)
                .currency(Currency.EUR)
                .customerId("456")
                .build();

        when(customerService.getCustomerById("456")).thenReturn(customer);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        when(accountDtoConverter.convert(updatedAccount)).thenReturn(expectedDto);

        AccountDto result = accountService.updateAccount(accountId, updateRequest);

        assertEquals(expectedDto, result);
        verify(customerService).getCustomerById("456");
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any(Account.class));
        verify(accountDtoConverter).convert(updatedAccount);
    }

    @Test
    void whenUpdateAccountCalledWithInvalidCustomer_itShouldReturnEmptyAccountDto() {
        String accountId = "123";
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setCustomerId("invalidCustomer");

        CustomerDto emptyCustomer = CustomerDto.builder().build();

        when(customerService.getCustomerById("invalidCustomer")).thenReturn(emptyCustomer);

        AccountDto result = accountService.updateAccount(accountId, updateRequest);

        assertEquals(AccountDto.builder().build(), result);
        verify(customerService).getCustomerById("invalidCustomer");
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void whenGetAllAccountsCalled_itShouldReturnAllAccountDtos() {
        Account account1 = Account.builder().id("1").balance(1000.0).build();
        Account account2 = Account.builder().id("2").balance(2000.0).build();
        List<Account> accounts = Arrays.asList(account1, account2);

        AccountDto dto1 = AccountDto.builder().id("1").balance(1000.0).build();
        AccountDto dto2 = AccountDto.builder().id("2").balance(2000.0).build();

        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountDtoConverter.convert(account1)).thenReturn(dto1);
        when(accountDtoConverter.convert(account2)).thenReturn(dto2);

        List<AccountDto> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(accountRepository).findAll();
        verify(accountDtoConverter, times(2)).convert(any(Account.class));
    }

    @Test
    void whenGetAccountByIdCalledWithExistingAccount_itShouldReturnAccountDto() {
        String accountId = "123";
        Account account = Account.builder().id(accountId).balance(1000.0).build();
        AccountDto expectedDto = AccountDto.builder().id(accountId).balance(1000.0).build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountDtoConverter.convert(account)).thenReturn(expectedDto);

        AccountDto result = accountService.getAccountById(accountId);

        assertEquals(expectedDto, result);
        verify(accountRepository).findById(accountId);
        verify(accountDtoConverter).convert(account);
    }

    @Test
    void whenGetAccountByIdCalledWithNonExistingAccount_itShouldReturnEmptyAccountDto() {
        String accountId = "nonexistent";

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountDto result = accountService.getAccountById(accountId);

        assertEquals(AccountDto.builder().build(), result);
        verify(accountRepository).findById(accountId);
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void whenDeleteAccountByIdCalled_itShouldCallRepositoryDelete() {
        String accountId = "123";

        accountService.deleteAccountById(accountId);

        verify(accountRepository).deleteById(accountId);
    }

    @Test
    void whenWithdrawMoneyCalledWithSufficientBalance_itShouldReturnUpdatedAccountDto() {
        String accountId = "123";
        double withdrawAmount = 500.0;
        Account account = Account.builder()
                .id(accountId)
                .balance(1000.0)
                .build();
        
        Account updatedAccount = Account.builder()
                .id(accountId)
                .balance(500.0)
                .build();

        AccountDto expectedDto = AccountDto.builder()
                .id(accountId)
                .balance(500.0)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        when(accountDtoConverter.convert(updatedAccount)).thenReturn(expectedDto);

        AccountDto result = accountService.withdrawMoney(accountId, withdrawAmount);

        assertEquals(expectedDto, result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any(Account.class));
        verify(accountDtoConverter).convert(updatedAccount);
    }

    @Test
    void whenWithdrawMoneyCalledWithInsufficientBalance_itShouldReturnEmptyAccountDto() {
        String accountId = "123";
        double withdrawAmount = 1500.0;
        Account account = Account.builder()
                .id(accountId)
                .balance(1000.0)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountDto result = accountService.withdrawMoney(accountId, withdrawAmount);

        assertEquals(AccountDto.builder().build(), result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void whenWithdrawMoneyCalledWithNonExistingAccount_itShouldReturnEmptyAccountDto() {
        String accountId = "nonexistent";
        double withdrawAmount = 500.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountDto result = accountService.withdrawMoney(accountId, withdrawAmount);

        assertEquals(AccountDto.builder().build(), result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void whenDepositMoneyCalledWithExistingAccount_itShouldReturnUpdatedAccountDto() {
        String accountId = "123";
        double depositAmount = 500.0;
        Account account = Account.builder()
                .id(accountId)
                .balance(1000.0)
                .build();
        
        Account updatedAccount = Account.builder()
                .id(accountId)
                .balance(1500.0)
                .build();

        AccountDto expectedDto = AccountDto.builder()
                .id(accountId)
                .balance(1500.0)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        when(accountDtoConverter.convert(updatedAccount)).thenReturn(expectedDto);

        AccountDto result = accountService.depositMoney(accountId, depositAmount);

        assertEquals(expectedDto, result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any(Account.class));
        verify(accountDtoConverter).convert(updatedAccount);
    }

    @Test
    void whenDepositMoneyCalledWithNonExistingAccount_itShouldReturnEmptyAccountDto() {
        String accountId = "nonexistent";
        double depositAmount = 500.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountDto result = accountService.depositMoney(accountId, depositAmount);

        assertEquals(AccountDto.builder().build(), result);
        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(accountDtoConverter);
    }
}