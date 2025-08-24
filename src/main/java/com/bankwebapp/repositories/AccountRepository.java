package com.bankwebapp.repositories;

import com.bankwebapp.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {


}
