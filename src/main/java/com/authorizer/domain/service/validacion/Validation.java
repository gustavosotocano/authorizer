package com.authorizer.domain.service.validacion;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;

import java.util.Optional;


  @FunctionalInterface
  public interface Validation {

    Optional<String> validate(final Transaction transaction,Account account);


}