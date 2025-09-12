package com.bankwebapp.repositories;

import com.bankwebapp.models.Account;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void whenSaveAccount_itShouldPersistAccount() {
        Account account = Account.builder()
                .id("test-account-1")
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("customer-1")
                .city(City.ANKARA)
                .build();

        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount);
        assertEquals("test-account-1", savedAccount.getId());
        assertEquals(1000.0, savedAccount.getBalance());
        assertEquals(Currency.USD, savedAccount.getCurrency());
        assertEquals("customer-1", savedAccount.getCustomerId());
        assertEquals(City.ANKARA, savedAccount.getCity());
    }

    @Test
    void whenFindById_withExistingAccount_itShouldReturnAccount() {
        Account account = Account.builder()
                .id("test-account-2")
                .balance(2000.0)
                .currency(Currency.EUR)
                .customerId("customer-2")
                .city(City.ISTANBUL)
                .build();

        entityManager.persistAndFlush(account);

        Optional<Account> foundAccount = accountRepository.findById("test-account-2");

        assertTrue(foundAccount.isPresent());
        assertEquals("test-account-2", foundAccount.get().getId());
        assertEquals(2000.0, foundAccount.get().getBalance());
        assertEquals(Currency.EUR, foundAccount.get().getCurrency());
        assertEquals("customer-2", foundAccount.get().getCustomerId());
        assertEquals(City.ISTANBUL, foundAccount.get().getCity());
    }

    @Test
    void whenFindById_withNonExistingAccount_itShouldReturnEmpty() {
        Optional<Account> foundAccount = accountRepository.findById("non-existing-account");

        assertFalse(foundAccount.isPresent());
    }

    @Test
    void whenFindAll_itShouldReturnAllAccounts() {
        Account account1 = Account.builder()
                .id("test-account-3")
                .balance(1500.0)
                .currency(Currency.USD)
                .customerId("customer-3")
                .city(City.ANKARA)
                .build();

        Account account2 = Account.builder()
                .id("test-account-4")
                .balance(2500.0)
                .currency(Currency.EUR)
                .customerId("customer-4")
                .city(City.ISTANBUL)
                .build();

        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);

        List<Account> accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.getId().equals("test-account-3")));
        assertTrue(accounts.stream().anyMatch(a -> a.getId().equals("test-account-4")));
    }

    @Test
    void whenDeleteById_itShouldRemoveAccount() {
        Account account = Account.builder()
                .id("test-account-5")
                .balance(3000.0)
                .currency(Currency.USD)
                .customerId("customer-5")
                .city(City.ANKARA)
                .build();

        entityManager.persistAndFlush(account);

        accountRepository.deleteById("test-account-5");

        Optional<Account> foundAccount = accountRepository.findById("test-account-5");
        assertFalse(foundAccount.isPresent());
    }

    @Test
    void whenUpdateAccount_itShouldPersistChanges() {
        Account account = Account.builder()
                .id("test-account-6")
                .balance(1000.0)
                .currency(Currency.USD)
                .customerId("customer-6")
                .city(City.ANKARA)
                .build();

        entityManager.persistAndFlush(account);

        Account foundAccount = accountRepository.findById("test-account-6").get();
        foundAccount.setBalance(1500.0);
        foundAccount.setCurrency(Currency.EUR);
        foundAccount.setCity(City.ISTANBUL);

        Account updatedAccount = accountRepository.save(foundAccount);

        assertEquals(1500.0, updatedAccount.getBalance());
        assertEquals(Currency.EUR, updatedAccount.getCurrency());
        assertEquals(City.ISTANBUL, updatedAccount.getCity());
    }
}