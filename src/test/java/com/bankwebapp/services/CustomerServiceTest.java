package com.bankwebapp.services;

import com.bankwebapp.dtos.*;
import com.bankwebapp.models.City;
import com.bankwebapp.models.Customer;
import com.bankwebapp.repositories.CustomerRepository;
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
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerDtoConverter customerDtoConverter;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void whenCreateCustomerCalledWithValidRequest_itShouldReturnValidCustomerDto() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setId("123");
        request.setName("John Doe");
        request.setAddres("123 Main St");
        request.setDateofBirth(1990);
        request.setCity(CityDto.ANKARA);

        Customer customer = new Customer();
        customer.setId("123");
        customer.setName("John Doe");
        customer.setAddres("123 Main St");
        customer.setDateofBirth(1990);
        customer.setCity(City.ANKARA);

        Customer savedCustomer = new Customer();
        savedCustomer.setId("123");
        savedCustomer.setName("John Doe");
        savedCustomer.setAddres("123 Main St");
        savedCustomer.setDateofBirth(1990);
        savedCustomer.setCity(City.ANKARA);

        CustomerDto expectedDto = CustomerDto.builder()
                .id("123")
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1990)
                .city(CityDto.ANKARA)
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerDtoConverter.getCustomerDto(savedCustomer)).thenReturn(expectedDto);

        CustomerDto result = customerService.createCustomer(request);

        assertEquals(expectedDto, result);
        verify(customerRepository).save(any(Customer.class));
        verify(customerDtoConverter).getCustomerDto(savedCustomer);
    }

    @Test
    void whenGetAllCustomersCalledWithExistingCustomers_itShouldReturnCustomerDtoList() {
        Customer customer1 = new Customer();
        customer1.setId("1");
        customer1.setName("John Doe");
        
        Customer customer2 = new Customer();
        customer2.setId("2");
        customer2.setName("Jane Smith");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        CustomerDto dto1 = CustomerDto.builder().id("1").name("John Doe").build();
        CustomerDto dto2 = CustomerDto.builder().id("2").name("Jane Smith").build();

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerDtoConverter.getCustomerDto(customer1)).thenReturn(dto1);
        when(customerDtoConverter.getCustomerDto(customer2)).thenReturn(dto2);

        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(customerRepository).findAll();
        verify(customerDtoConverter, times(2)).getCustomerDto(any(Customer.class));
    }

    @Test
    void whenGetCustomerByIdCalledWithExistingCustomer_itShouldReturnCustomerDto() {
        String customerId = "123";
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");

        CustomerDto expectedDto = CustomerDto.builder()
                .id(customerId)
                .name("John Doe")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerDtoConverter.getCustomerDto(customer)).thenReturn(expectedDto);

        CustomerDto result = customerService.getCustomerById(customerId);

        assertEquals(expectedDto, result);
        verify(customerRepository).findById(customerId);
        verify(customerDtoConverter).getCustomerDto(customer);
    }

    @Test
    void whenGetCustomerByIdCalledWithNonExistingCustomer_itShouldReturnEmptyCustomerDto() {
        String customerId = "nonexistent";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerDto result = customerService.getCustomerById(customerId);

        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerDtoConverter, never()).getCustomerDto(any());
    }

    @Test
    void whenDeleteCustomerByIdCalled_itShouldCallRepositoryDelete() {
        String customerId = "123";

        customerService.deleteCustomerById(customerId);

        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void whenUpdateCustomerCalledWithExistingCustomer_itShouldUpdateAndReturnCustomerDto() {
        String customerId = "123";
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        request.setAddres("Updated Address");
        request.setDateofBirth(1995);
        request.setCity(CityDto.ISTANBUL);

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setName("Old Name");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setName("Updated Name");
        updatedCustomer.setAddres("Updated Address");
        updatedCustomer.setDateofBirth(1995);
        updatedCustomer.setCity(City.ISTANBUL);

        CustomerDto expectedDto = CustomerDto.builder()
                .id(customerId)
                .name("Updated Name")
                .addres("Updated Address")
                .dateofBirth(1995)
                .city(CityDto.ISTANBUL)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerDtoConverter.getCustomerDto(any(Customer.class))).thenReturn(expectedDto);

        CustomerDto result = customerService.updateCustomer(customerId, request);

        assertEquals(expectedDto, result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(any(Customer.class));
        verify(customerDtoConverter).getCustomerDto(any(Customer.class));
    }

    @Test
    void whenUpdateCustomerCalledWithNonExistingCustomer_itShouldReturnEmptyCustomerDto() {
        String customerId = "nonexistent";
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerDto result = customerService.updateCustomer(customerId, request);

        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).save(any());
    }
}