package com.authorizer.domain.service.validacion;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InsufficientLimit implements Validation {


   public Optional<String> validate(final Transaction transaction, Account account) {

     if (transaction.getTransaction().getAmount() <= account.getAccount().getAvailableLimit()) {
       return Optional.empty();

     }

    return Optional.of("insufficient-limit");
  }
}



