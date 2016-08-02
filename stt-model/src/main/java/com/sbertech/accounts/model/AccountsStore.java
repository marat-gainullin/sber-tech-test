package com.sbertech.accounts.model;

import java.util.Collection;

/**
 * Accounts store abstraction interface.
 *
 * @author mg
 */
public interface AccountsStore {

    /**
     * Adds an account to the store.
     *
     * @param aAccount A account to be added.
     */
    void add(Account aAccount);

    /**
     * Finds an account by number.
     *
     * @param aAccountNumber A account number to search by.
     * @return Account instance found.
     */
    Account find(String aAccountNumber);

}
