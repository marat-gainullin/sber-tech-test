package com.sbertech.accounts.services;

import com.sbertech.accounts.model.Account;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.sbertech.accounts.model.Transfer;
import org.springframework.transaction.annotation.Transactional;
import com.sbertech.accounts.model.TransfersStore;
import javax.persistence.TypedQuery;

/**
 * Transfers database store bean.
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addTransfer(final Transfer aTransfer,
            final Account aSourceAccount,
            final Account aDestAccount) {
        dataStore.merge(aSourceAccount);
        dataStore.merge(aDestAccount);
        dataStore.persist(aTransfer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transfer find(final Long aTransferId) {
        TypedQuery<Transfer> fetcher = dataStore.createNamedQuery(
                "transfer.by.id", Transfer.class);
        fetcher.setParameter("id", aTransferId);
        return fetcher.getSingleResult();
    }

}
