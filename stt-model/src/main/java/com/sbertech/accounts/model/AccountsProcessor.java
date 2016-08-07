package com.sbertech.accounts.model;

import com.sbertech.accounts.exceptions.EmptyTransferException;
import com.sbertech.accounts.exceptions.SameAccountsTransferException;
import com.sbertech.accounts.exceptions.NotEnoughAmountException;
import java.io.IOException;
import java.util.Collection;

/**
 * Account processing abstraction interface.
 *
 * @author mg
 */
public interface AccountsProcessor {

    /**
     * Performs withdraw from an account with {@code aSourceAccountNumber} to
     * {@code aDestAccountNumber} in a fastest way may be with some
     * speculations.
     *
     * @param aTransfer Atranfer to be performed.
     * @throws NotEnoughAmountException if not enough amount on source account
     * for withdraw.
     * @throws EmptyTransferException if {@code aTransfer} has zero amount.
     * @throws SameAccountsTransferException when an attempt of transfer from an
     * account to itself is made.
     * @throws IOException if some error while communications occur.
     */
    void transfer(Transfer aTransfer) throws NotEnoughAmountException,
            EmptyTransferException,
            SameAccountsTransferException,
            IOException;

    /**
     * Retrieves transfers from or to an account. Account's operations are
     * treated as a set of transfers.
     *
     * @param aAccountNumber A account number wich transfers should be fetched.
     * @return A {@code Collection} of transfers from or to the account.
     * @throws IOException if some error while communications occur.
     */
    Collection<Transfer> transfersOnAccount(String aAccountNumber)
            throws IOException;

}
