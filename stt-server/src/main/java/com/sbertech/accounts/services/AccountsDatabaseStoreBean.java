package com.sbertech.accounts.services;

import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsStore;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Accounts database store bean.
 *
 * @author mg
 */
@Transactional
@Repository
public class AccountsDatabaseStoreBean implements AccountsStore {

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    @Override
    public Account find(String aAccountNumber) {
        TypedQuery<Account> fetcher = dataStore.createNamedQuery("account.by.number", Account.class);
        fetcher.setParameter("accountNumber", aAccountNumber);
        return fetcher.getSingleResult();
    }

    @Override
    public Collection<Account> accounts() {
        TypedQuery<Account> fetcher = dataStore.createNamedQuery("accounts.all", Account.class);
        return fetcher.getResultList();
    }

}
