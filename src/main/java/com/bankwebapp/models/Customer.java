package com.bankwebapp.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    private String id;
    private String name;
    private Integer dateofBirth;
    private String addres;
    private City city;
}
