package com.sbertech.accounts.model;

import java.io.IOException;
import java.util.Collection;

/**
 * Accounts store abstraction interface.
 *
 * @author mg
 */
public interface AccountsStore {

    /**
     * Retrieves accounts collection.
     *
     * @return Accounts collection.
     * @throws IOException if some error while communications occur.
     */
    Collection<Account> accounts() throws IOException;

    /**
     * Finds an account by number.
     *
     * @param aAccountNumber A account number to search by.
     * @return Account instance found.
     * @throws IOException if some error while communications occur.
     */
    Account find(String aAccountNumber) throws IOException;

}
