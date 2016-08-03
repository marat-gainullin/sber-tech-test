package com.sbertech.accounts.model;

/**
 * Transfers store abstraction interface.
 *
 * @author mg
 */
public interface OperationsStore {

    /**
     * Adds a {@code Transfer} instance to persistent store.
     *
     * @param aTransfer a {@code Transfer} instance to save.
     */
    void addTransfer(Transfer aTransfer);

    /**
     * Fins a {@code Transfer} by it's primary key.
     *
     * @param aTransferId A {@code Transfer} primary key to lookup.
     * @return A {@code Transfer} instance found.
     */
    Transfer find(String aTransferId);
}
