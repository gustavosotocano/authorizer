package com.authorizer.domain.service.validacion;

import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LastThreeTransaction implements Validation {

  private final TransactionRepository transactionRepository;

    public LastThreeTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public Optional<String> validate(final Transaction post, Account account) {

    if (transactionRepository.lastTransactions(post).size() <2) {
      return Optional.empty();

    }
    return Optional.of("high-frequency-small-interval");
  }
}



