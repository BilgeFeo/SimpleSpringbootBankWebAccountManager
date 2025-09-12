package com.bankwebapp.controllers;

import com.bankwebapp.dtos.*;
import com.bankwebapp.services.CustomerService;
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

@WebMvcTest(CustomesController.class)
class CustomesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenHelloWorldCalled_itShouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/customer/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN + ";charset=UTF-8"))
                .andExpect(content().string("Hello World"));
    }

    @Test
    void whenCreateCustomerCalled_itShouldCreateAndReturnCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setId("123");
        request.setName("John Doe");
        request.setAddres("123 Main St");
        request.setDateofBirth(1990);
        request.setCity(CityDto.ANKARA);

        CustomerDto expectedCustomer = CustomerDto.builder()
                .id("123")
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1990)
                .city(CityDto.ANKARA)
                .build();

        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(expectedCustomer);

        mockMvc.perform(post("/customer/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.addres").value("123 Main St"))
                .andExpect(jsonPath("$.dateofBirth").value(1990))
                .andExpect(jsonPath("$.city").value("ANKARA"));

        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    void whenGetAllCustomersCalled_itShouldReturnCustomerList() throws Exception {
        CustomerDto customer1 = CustomerDto.builder()
                .id("1")
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1990)
                .city(CityDto.ANKARA)
                .build();
        
        CustomerDto customer2 = CustomerDto.builder()
                .id("2")
                .name("Jane Smith")
                .addres("456 Oak Ave")
                .dateofBirth(1985)
                .city(CityDto.ISTANBUL)
                .build();

        List<CustomerDto> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].addres").value("123 Main St"))
                .andExpect(jsonPath("$[0].dateofBirth").value(1990))
                .andExpect(jsonPath("$[0].city").value("ANKARA"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].addres").value("456 Oak Ave"))
                .andExpect(jsonPath("$[1].dateofBirth").value(1985))
                .andExpect(jsonPath("$[1].city").value("ISTANBUL"));

        verify(customerService).getAllCustomers();
    }

    @Test
    void whenGetCustomerByIdCalled_itShouldReturnCustomer() throws Exception {
        String customerId = "123";
        CustomerDto customer = CustomerDto.builder()
                .id(customerId)
                .name("John Doe")
                .addres("123 Main St")
                .dateofBirth(1990)
                .city(CityDto.ANKARA)
                .build();

        when(customerService.getCustomerById(customerId)).thenReturn(customer);

        mockMvc.perform(get("/customer/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.addres").value("123 Main St"))
                .andExpect(jsonPath("$.dateofBirth").value(1990))
                .andExpect(jsonPath("$.city").value("ANKARA"));

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    void whenDeleteCustomerCalled_itShouldDeleteCustomer() throws Exception {
        String customerId = "123";

        mockMvc.perform(delete("/customer/{id}", customerId))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(customerId);
    }

    @Test
    void whenUpdateCustomerCalled_itShouldUpdateAndReturnCustomer() throws Exception {
        String customerId = "123";
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        request.setAddres("Updated Address");
        request.setDateofBirth(1995);
        request.setCity(CityDto.ISTANBUL);

        CustomerDto expectedCustomer = CustomerDto.builder()
                .id(customerId)
                .name("Updated Name")
                .addres("Updated Address")
                .dateofBirth(1995)
                .city(CityDto.ISTANBUL)
                .build();

        when(customerService.updateCustomer(eq(customerId), any(UpdateCustomerRequest.class))).thenReturn(expectedCustomer);

        mockMvc.perform(put("/customer/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.addres").value("Updated Address"))
                .andExpect(jsonPath("$.dateofBirth").value(1995))
                .andExpect(jsonPath("$.city").value("ISTANBUL"));

        verify(customerService).updateCustomer(eq(customerId), any(UpdateCustomerRequest.class));
    }
}