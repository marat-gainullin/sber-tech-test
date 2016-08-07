package com.sbertech.accounts.exceptions;

/**
 * Exception thrown when a {@code Transfer} with sero amount is handled.
 *
 * @author mg
 */
public class EmptyTransferException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 1238749592824636267L;

    /**
     * The exception no args constructor.
     */
    public EmptyTransferException() {
        super("A transfer with zero amount is prohibited.");
    }
}
