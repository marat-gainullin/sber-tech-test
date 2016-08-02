package com.sbertech.accounts.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sbertech.accounts.model.NotEnoughAmountException;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.model.Transfer;
import com.sbertech.accounts.model.OperationsStore;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Processor implementation. Performs withdraw transactions. Transactions
 * handled with this processor is treated as withdraw only transactions. Though
 * they have destination account number and information about account's amount
 * growth will be preserved.
 *
 * @author mg
 */
@Service
@Transactional
public class AccountsProcessorBean implements AccountsProcessor {

    /**
     * Accounts store.
     */
    @Autowired
    private AccountsStore accountsStore;

    /**
     * Operations store.
     */
    @Autowired
    private OperationsStore transfersStore;

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    /**
     * Map of accounts considered as withdraw sources. Ocasionally, transfer
     * destination accounts may appear in this map because of other withdraw
     * operations. In this case they are abandened with same synchronization
     * point as when withdraw is performed.
     */
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void transfer(final Transfer aTransfer) throws NotEnoughAmountException {
        boolean abandoned;
        do {
            final Account sourceAccount = accounts.computeIfAbsent(aTransfer.getFromAccountNumber(), (String aAccountNumber) -> {
                return accountsStore.find(aAccountNumber);
            });
            abandoned = sourceAccount.withdraw(aTransfer.getAmount(), () -> {
                transfersStore.addTransfer(aTransfer);
            });
        } while (abandoned);
        final Account destAccount = accounts.get(aTransfer.getToAccountNumber());
        if (destAccount != null) {
            destAccount.abandone(() -> {
                accounts.remove(aTransfer.getToAccountNumber());
            });
        }
    }

    @Override
    public Collection<Transfer> transfersOnAccount(String aAccountNumber) {
        Query fetcher = dataStore.createNamedQuery("transfers.by.account");
        fetcher.setParameter("accountNumber", aAccountNumber);
        return (Collection<Transfer>) fetcher.getResultList();
    }

}
