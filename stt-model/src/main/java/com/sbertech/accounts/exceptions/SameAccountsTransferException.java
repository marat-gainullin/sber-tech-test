package com.sbertech.accounts.exceptions;

import java.text.MessageFormat;

/**
 * Exception throw when an attempt of transfer from an account to itself is
 * made.
 *
 * @author mg
 */
public class SameAccountsTransferException extends Exception {

    /**
     * Generated seril version UID.
     */
    private static final long serialVersionUID = 1152122743588824445L;

    /**
     * Constructor with an account number.
     *
     * @param anAccountNumber The account number, the problematic transfer is
     * about.
     */
    public SameAccountsTransferException(final String anAccountNumber) {
        super(MessageFormat
                .format("Transfers from {0} account to itself is prohibited.",
                        anAccountNumber));
    }
}
