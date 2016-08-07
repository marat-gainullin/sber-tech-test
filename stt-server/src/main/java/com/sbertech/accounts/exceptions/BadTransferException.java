package com.sbertech.accounts.exceptions;

import java.text.MessageFormat;

/**
 * Exception thown when no account found, a transaction has bad data (e.g. null
 * source or to account number or null amount).
 *
 * @author mg
 */
public class BadTransferException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 5561114535729550804L;

    /**
     * The exception constructor.
     *
     * @param aDetails A detais message about what is wrong.
     */
    public BadTransferException(final String aDetails) {
        super(MessageFormat.format("Bad transfer. {0}", aDetails));
    }

}
