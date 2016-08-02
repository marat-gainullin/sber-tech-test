package com.sbertech.accounts.model;

import java.text.MessageFormat;

/**
 * Excpetion thrown when amout is not enough for transaction.
 *
 * @author mg
 */
public class NotEnoughAmountException extends Exception {

    /**
     * Constructor with information on account and amount needed for
     * transaction.
     *
     * @param aAccountId A account id.
     */
    public NotEnoughAmountException(final String aAccountId) {
        super(MessageFormat.format(
                "Amount on account {0} is not enough to perform a transaction",
                aAccountId));
    }
}
