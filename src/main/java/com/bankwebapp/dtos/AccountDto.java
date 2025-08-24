package com.bankwebapp.dtos;
import com.bankwebapp.models.Currency;
import lombok.*;


@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private String id;
    private String customerId;
    private double balance;
    private Currency currency;


}
