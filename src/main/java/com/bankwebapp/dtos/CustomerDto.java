package com.bankwebapp.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CustomerDto {

    private String id;
    private String name;
    private Integer dateofBirth;
    private String addres;
    private CityDto city;


}
