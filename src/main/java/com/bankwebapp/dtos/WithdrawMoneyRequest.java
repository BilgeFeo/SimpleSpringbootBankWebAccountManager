package com.bankwebapp.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class WithdrawMoneyRequest {

    String id;
    Double amount;

}
