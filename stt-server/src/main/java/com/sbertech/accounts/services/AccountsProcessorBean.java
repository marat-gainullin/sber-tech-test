package com.sbertech.accounts.services;

import com.sbertech.accounts.exceptions.SameAccountsTransferException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbertech.accounts.exceptions.NotEnoughAmountException;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.exceptions.EmptyTransferException;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.sbertech.accounts.model.TransfersStore;
import java.text.MessageFormat;
import javax.persistence.NoResultException;

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
     * Transfers store.
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
     * @throws EmptyTransferException if {@code aTransfer} has zero amount.
     * @throws SameAccountsTransferException when an attempt of transfer from an
     * account to itself is made.
     * @throws IOException if some error while communications occur.
     */
    @Override
    @SuppressWarnings("NestedSynchronizedStatement")
    public final void transfer(final Transfer aTransfer)
            throws NotEnoughAmountException,
            EmptyTransferException,
            IOException,
            SameAccountsTransferException {
        /**
         * Since Oracle attempts to eliminate byte-range interning of numbers,
         * ignore interned numbers cache.
         */
        if (aTransfer.getAmount().equals(0L)) {
            throw new EmptyTransferException();
        }
        if (aTransfer.getFromAccount().equals(aTransfer.getToAccount())) {
            throw new SameAccountsTransferException(aTransfer.getFromAccount());
        }
        shrinkAccounts();
        Function<String, Account> fromDatabase = (String aAccountNumber) -> {
            try {
                return accountsStore.find(aAccountNumber);
            } catch (IOException ex) {
                Logger.getLogger(AccountsProcessorBean.class.getName())
                        .log(Level.SEVERE, null, ex);
                throw new UncheckedIOException(ex);
            }
        };
        try {
            boolean abandoned;
            do {
                abandoned = false;
                Account sourceAccount = accounts
                        .computeIfAbsent(aTransfer.getFromAccount(),
                                fromDatabase);
                Account destAccount = accounts
                        .computeIfAbsent(aTransfer.getToAccount(),
                                fromDatabase);
                final long amount = aTransfer.getAmount();
                final Account first = sourceAccount.getAccountNumber()
                        .compareTo(destAccount.getAccountNumber()) < 0
                        ? sourceAccount : destAccount;
                final Account second = sourceAccount.getAccountNumber()
                        .compareTo(destAccount.getAccountNumber()) < 0
                        ? destAccount : sourceAccount;
                synchronized (first) {
                    synchronized (second) {
                        if (!first.abandoned() && !second.abandoned()) {
                            if (sourceAccount.getAmount() < amount) {
                                throw new NotEnoughAmountException(
                                        sourceAccount.getAccountNumber());
                            }
                            sourceAccount.add(-amount);
                            destAccount.add(amount);
                            try {
                                transfersStore.addTransfer(aTransfer,
                                        sourceAccount, destAccount);
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

    /**
     * Shrinks accounts map to avoid too large memory consumption.
     */
    private void shrinkAccounts() {
        if (accounts.size() > MAX_ACCOUNTS_SIZE) {
            Account[] toAbandone = accounts.values().toArray(new Account[]{});
            for (Account account : toAbandone) {
                account.abandone();
                /**
                 * In the cae of new account arrive for the same key, two
                 * arguments remove method will take care of such case.
                 */
                accounts.remove(account.getAccountNumber(), account);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<Transfer> transfersOnAccount(
            final String aAccountNumber) throws IOException {
        accountsStore.find(aAccountNumber);
        TypedQuery<Transfer> fetcher = dataStore
                .createNamedQuery("transfers.byaccount", Transfer.class);
        fetcher.setParameter("account", aAccountNumber);
        return fetcher.getResultList();
    }

}
