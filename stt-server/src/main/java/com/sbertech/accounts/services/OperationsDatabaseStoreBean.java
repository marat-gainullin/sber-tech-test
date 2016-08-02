package com.sbertech.accounts.services;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sbertech.accounts.model.Transfer;
import com.sbertech.accounts.model.OperationsStore;

/**
 * Operations database store bean.
 *
 * @author mg
 */
@Repository
@Transactional
public class OperationsDatabaseStoreBean implements OperationsStore {

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    @Override
    public void addTransfer(Transfer aTransfer) {
        dataStore.persist(aTransfer);
    }

    @Override
    public Transfer find(String aTransferId) {
        return dataStore.find(Transfer.class, aTransferId);
    }

}
