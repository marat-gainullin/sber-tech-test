package com.sbertech.accounts.services;

import com.sbertech.accounts.model.Account;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.sbertech.accounts.model.Transfer;
import org.springframework.transaction.annotation.Transactional;
import com.sbertech.accounts.model.TransfersStore;

/**
 * Operations database store bean.
 *
 * @author mg
 */
@Transactional
@Repository
public class TransfersDatabaseStoreBean implements TransfersStore {

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    @Override
    public void addTransfer(Transfer aTransfer, Account aSourceAccount, Account aDestAccount) {
        dataStore.merge(aSourceAccount);
        dataStore.merge(aDestAccount);
        dataStore.persist(aTransfer);
    }

    @Override
    public Transfer find(Long aTransferId) {
        return dataStore.find(Transfer.class, aTransferId);
    }

}
