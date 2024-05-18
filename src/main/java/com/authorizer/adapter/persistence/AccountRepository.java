package com.authorizer.adapter.persistence;

import com.authorizer.domain.model.Transaction;
import com.authorizer.domain.model.Account;

/**
 * The repository interface is an outbound port that enables communication from the core application to a database
 */
public interface AccountRepository {

    Account addAccount(Account account);

    Account getAccountById(Integer accountId);

    Account updateAvailableLimit(Transaction transaction);

    void remove();

}
