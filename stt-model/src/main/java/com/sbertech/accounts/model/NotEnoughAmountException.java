package com.sbertech.accounts.model;

import java.text.MessageFormat;

/**
 * Exception thrown when amount is not enough for transaction.
 *
 * @author mg
 */
public class NotEnoughAmountException extends Exception {

    /**
     * Constructor with information on account and amount needed for
     * transaction.
     *
     * @param aAccount An account number.
     */
    public NotEnoughAmountException(final String aAccount) {
        super(MessageFormat.format(
                "Amount on account {0} is not enough to perform a transaction",
                aAccount));
    }
}
