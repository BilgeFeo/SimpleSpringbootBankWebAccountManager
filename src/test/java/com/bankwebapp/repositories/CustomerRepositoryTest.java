package com.bankwebapp.repositories;

import com.bankwebapp.models.City;
import com.bankwebapp.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void whenSaveCustomer_itShouldPersistCustomer() {
        Customer customer = new Customer();
        customer.setId("test-customer-1");
        customer.setName("John Doe");
        customer.setAddres("123 Main St");
        customer.setDateofBirth(1990);
        customer.setCity(City.ANKARA);

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertEquals("test-customer-1", savedCustomer.getId());
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("123 Main St", savedCustomer.getAddres());
        assertEquals(1990, savedCustomer.getDateofBirth());
        assertEquals(City.ANKARA, savedCustomer.getCity());
    }

    @Test
    void whenFindById_withExistingCustomer_itShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setId("test-customer-2");
        customer.setName("Jane Smith");
        customer.setAddres("456 Oak Ave");
        customer.setDateofBirth(1985);
        customer.setCity(City.ISTANBUL);

        entityManager.persistAndFlush(customer);

        Optional<Customer> foundCustomer = customerRepository.findById("test-customer-2");

        assertTrue(foundCustomer.isPresent());
        assertEquals("test-customer-2", foundCustomer.get().getId());
        assertEquals("Jane Smith", foundCustomer.get().getName());
        assertEquals("456 Oak Ave", foundCustomer.get().getAddres());
        assertEquals(1985, foundCustomer.get().getDateofBirth());
        assertEquals(City.ISTANBUL, foundCustomer.get().getCity());
    }

    @Test
    void whenFindById_withNonExistingCustomer_itShouldReturnEmpty() {
        Optional<Customer> foundCustomer = customerRepository.findById("non-existing-customer");

        assertFalse(foundCustomer.isPresent());
    }

    @Test
    void whenFindAll_itShouldReturnAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId("test-customer-3");
        customer1.setName("Bob Johnson");
        customer1.setAddres("789 Pine St");
        customer1.setDateofBirth(1992);
        customer1.setCity(City.ANKARA);

        Customer customer2 = new Customer();
        customer2.setId("test-customer-4");
        customer2.setName("Alice Brown");
        customer2.setAddres("321 Elm St");
        customer2.setDateofBirth(1988);
        customer2.setCity(City.ISTANBUL);

        entityManager.persistAndFlush(customer1);
        entityManager.persistAndFlush(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertEquals(2, customers.size());
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals("test-customer-3")));
        assertTrue(customers.stream().anyMatch(c -> c.getId().equals("test-customer-4")));
    }

    @Test
    void whenDeleteById_itShouldRemoveCustomer() {
        Customer customer = new Customer();
        customer.setId("test-customer-5");
        customer.setName("Charlie Wilson");
        customer.setAddres("555 Maple Ave");
        customer.setDateofBirth(1995);
        customer.setCity(City.ANKARA);

        entityManager.persistAndFlush(customer);

        customerRepository.deleteById("test-customer-5");

        Optional<Customer> foundCustomer = customerRepository.findById("test-customer-5");
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    void whenUpdateCustomer_itShouldPersistChanges() {
        Customer customer = new Customer();
        customer.setId("test-customer-6");
        customer.setName("David Lee");
        customer.setAddres("777 Oak St");
        customer.setDateofBirth(1980);
        customer.setCity(City.ANKARA);

        entityManager.persistAndFlush(customer);

        Customer foundCustomer = customerRepository.findById("test-customer-6").get();
        foundCustomer.setName("David Lee Updated");
        foundCustomer.setAddres("888 Pine Ave");
        foundCustomer.setDateofBirth(1981);
        foundCustomer.setCity(City.ISTANBUL);

        Customer updatedCustomer = customerRepository.save(foundCustomer);

        assertEquals("David Lee Updated", updatedCustomer.getName());
        assertEquals("888 Pine Ave", updatedCustomer.getAddres());
        assertEquals(1981, updatedCustomer.getDateofBirth());
        assertEquals(City.ISTANBUL, updatedCustomer.getCity());
    }
}