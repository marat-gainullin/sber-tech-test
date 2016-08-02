package com.sbertech.accounts.exceptions;

import java.text.MessageFormat;

/**
 * Exception thown when no account found, a transaction is to be done against.
 *
 * @author mg
 */
public class AccountNotFoundException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 5561114535729550804L;

    /**
     * The exception constructor.
     *
     * @param aAccountNumber An account id.
     */
    public AccountNotFoundException(final String aAccountNumber) {
        super(MessageFormat.format("Account with number {0} is not found.", aAccountNumber));
    }

}
