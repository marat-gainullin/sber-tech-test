package com.sbertech.accounts.model;

/**
 * Transfers store abstraction interface.
 *
 * @author mg
 */
public interface TransfersStore {

    /**
     * Adds a {@code Transfer} instance to persistent store.
     *
     * @param aTransfer a {@code Transfer} instance to save.
     * @param aSourceAccount A source account of the transfer.
     * @param aDestAccount A destination account of the transfer.
     */
    void addTransfer(Transfer aTransfer,
            Account aSourceAccount, Account aDestAccount);

    /**
     * Fins a {@code Transfer} by it's primary key.
     *
     * @param aTransferId A {@code Transfer} primary key to lookup.
     * @return A {@code Transfer} instance found.
     */
    Transfer find(Long aTransferId);
}
