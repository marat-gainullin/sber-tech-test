package com.sbertech.accounts.services;

import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsStore;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Accounts database store bean.
 *
 * @author mg
 */
@Repository
@Transactional
public class AccountsDatabaseStoreBean implements AccountsStore {

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    @Override
    public void add(Account aAccount) {
        dataStore.persist(aAccount);
    }

    @Override
    public Account find(String aAccountNumber) {
        Query fetcher = dataStore.createNamedQuery("account.by.number");
        fetcher.setParameter("accountNumber", aAccountNumber);
        return (Account) fetcher.getSingleResult();
    }

}
