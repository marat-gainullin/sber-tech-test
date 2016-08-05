package com.sbertech.accounts.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbertech.accounts.model.NotEnoughAmountException;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.sbertech.accounts.model.TransfersStore;

/**
 * Processor implementation. Performs inter accounts transfer transactions.
 *
 * @author mg
 */
@Service
public class AccountsProcessorBean implements AccountsProcessor {

    /**
     * Maximum size of stripped locks map of accounts.
     */
    private static final int MAX_ACCOUNTS_SIZE = 1000;

    /**
     * Accounts store.
     */
    @Autowired
    private AccountsStore accountsStore;

    /**
     * Operations store.
     */
    @Autowired
    private TransfersStore transfersStore;

    /**
     * JPA data access layer.
     */
    @PersistenceContext
    private EntityManager dataStore;

    /**
     * Map of accounts considered as withdraw sources. Occasionally, transfer
     * destination accounts may appear in this map because of other withdraw
     * operations. In this case they are abandoned with same synchronization
     * point as when withdraw is performed.
     */
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Transfers an amount from source account to destination account, according
     * to {@code aTransfer}. Applies nested locks for amounts operations. To
     * avoid deadlocks it ensures the order in which locks are acquired. The
     * locks are acquired in order of accounts numbers.
     *
     * @param aTransfer {@code Transfer} instance with information about a
     * transaction.
     * @throws NotEnoughAmountException if amount on an account is less then
     * transfer amount.
     * @throws IOException if some error while communications occur.
     */
    @Override
    @SuppressWarnings("NestedSynchronizedStatement")
    public void transfer(final Transfer aTransfer) throws NotEnoughAmountException,
            IOException {
        shrinkAccounts();
        Function<String, Account> fromDatabase = (String aAccountNumber) -> {
            try {
                return accountsStore.find(aAccountNumber);
            } catch (IOException ex) {
                Logger.getLogger(AccountsProcessorBean.class.getName()).log(Level.SEVERE, null, ex);
                throw new UncheckedIOException(ex);
            }
        };
        try {
            boolean abandoned;
            do {
                abandoned = false;
                Account sourceAccount = accounts.computeIfAbsent(aTransfer.getFromAccount(), fromDatabase);
                Account destAccount = accounts.computeIfAbsent(aTransfer.getToAccount(), fromDatabase);
                final long amount = aTransfer.getAmount();
                final Account first = sourceAccount.getAccountNumber().compareTo(destAccount.getAccountNumber()) < 0 ? sourceAccount : destAccount;
                final Account second = sourceAccount.getAccountNumber().compareTo(destAccount.getAccountNumber()) < 0 ? destAccount : sourceAccount;
                synchronized (first) {
                    synchronized (second) {
                        if (!first.abandoned() && !second.abandoned()) {
                            if (sourceAccount.getAmount() < amount) {
                                throw new NotEnoughAmountException(sourceAccount.getAccountNumber());
                            }
                            sourceAccount.add(-amount);
                            destAccount.add(amount);
                            try {
                                transfersStore.addTransfer(aTransfer, sourceAccount, destAccount);
                            } catch (Exception ex) {
                                sourceAccount.add(amount);
                                destAccount.add(-amount);
                                throw ex;
                            }
                        } else {
                            abandoned = true;
                        }
                    }
                }
            } while (abandoned);
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

    private void shrinkAccounts() {
        if (accounts.size() > MAX_ACCOUNTS_SIZE) {
            Iterator<Map.Entry<String, Account>> ai = accounts.entrySet().iterator();
            while (ai.hasNext()) {
                Account account = ai.next().getValue();
                account.abandone();
                ai.remove();
            }
        }
    }

    @Override
    public Collection<Transfer> transfersOnAccount(String aAccountNumber) {
        TypedQuery<Transfer> fetcher = dataStore.createNamedQuery("transfers.byaccount", Transfer.class);
        fetcher.setParameter("account", aAccountNumber);
        return fetcher.getResultList();
    }

}
