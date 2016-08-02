package com.sbertech.accounts.model;

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
     */
    void transfer(Transfer aTransfer) throws NotEnoughAmountException;
    
    /**
     * Retrieves account's operations.
     *
     * @param aAccountNumber A account number wich operations should be fetched.
     * @return An {@code Collection} on operation of the account.
     */
    Collection<Transfer> transfersOnAccount(String aAccountNumber);

}
