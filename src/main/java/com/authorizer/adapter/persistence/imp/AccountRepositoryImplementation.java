package com.authorizer.adapter.persistence.imp;

import com.authorizer.adapter.persistence.AccountRepository;
import com.authorizer.domain.model.Transaction;
import com.authorizer.domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * The interface defines an output adapter which is an implementation of the outbound port. It enables the core application to communicate to external dependency such as the database.
 */
@Repository
public class AccountRepositoryImplementation implements AccountRepository {

  private static final Map<Integer, Account> accountMap = new HashMap<Integer, Account>(0);

  @Override
  public Account getAccountById(Integer accountId) {
    return accountMap.get(accountId);
  }

  @Override
  public Account updateAvailableLimit(Transaction transaction) {
    Account account = getAccountById(1);
    synchronized(account) {
      int available = account.getAccount().getAvailableLimit() - transaction.getTransaction().getAmount();
      account.getAccount().setAvailableLimit(available);
    }
    return account;
  }

  @Override public void remove() {
    if (null!=accountMap) {
      accountMap.clear();
    }
  }

  @Override public Account addAccount(Account account) {

    accountMap.put(account.getAccount().getAccountId(), account);

    return account;
  }

}
