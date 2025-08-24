package com.bankwebapp.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account {
    @Id
    private String id;
    private String customerId;
    private double balance;
    private City city;
    private Currency currency;


}
//