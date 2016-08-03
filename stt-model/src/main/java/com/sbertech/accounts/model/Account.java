package com.sbertech.accounts.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Account persistent POJO. It is persitent entity.
 *
 * @author mg
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "account.by.number", query = ""
            + " from Account a"
            + " where a.accountNumber = :accountNumber"),
    @NamedQuery(name = "accounts.all", query = ""
            + " from Account a")})
public class Account implements Serializable {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 3471069051927286254L;

    /**
     * Account number.
     */
    @Id
    private String accountNumber;

    /**
     * The account's amount. This value is per cent accurate.
     */
    private long amount;

    /**
     * Idicates abandened state. May only be switched on once.
     */
    private transient boolean abandoned;

    /**
     * Account's description.
     */
    private String description;

    public Account() {
        super();
    }

    public Account(String aAccountNumber, long aAmount, String aDescription) {
        super();
        accountNumber = aAccountNumber;
        amount = aAmount;
        description = aDescription;
    }

    /**
     * Amount getter.
     *
     * @return account amount.
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Account number getter.
     *
     * @return this account number.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Account's description getter.
     *
     * @return account's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Account's description setter.
     *
     * @param aDescription A account's description to be set.
     */
    public void setDescription(String aDescription) {
        description = aDescription;
    }

    /**
     * Returns whether this instance of {@code Account} is abandoned.
     *
     * @return {@code true} if this instance of {@code Account} is abandoned and
     * {@code false} otherwise.
     */
    public boolean isAbandoned() {
        return abandoned;
    }

    /**
     * Abandenes this {@code Account} instance.
     *
     * First it checks whether it is abandoned already. Without such check,
     * {@code aOnAbandoned} may remove new account with the same account number,
     * that was concurrently added by another thread.
     *
     * @param aOnAbandoned A piece of client code executed inside our
     * synchronized block.
     */
    public synchronized void abandone(Runnable aOnAbandoned) {
        if (!abandoned) {
            abandoned = true;
            aOnAbandoned.run();
        }
    }

    /**
     * Withdraws an amount from this account. It may fail if this instanceof
     * {@code Account} is abandoned and it may throw an exception if account has
     * not enough amount.
     *
     * @param aAmount Amount to withdraw from this account.
     * @param aOnWithdraw A piece of client code executed inside our
     * synchronized block.
     * @return {@code True} if this instanceof {@code Account} has been
     * abandoned, and {@code False} otherwise.
     * @throws NotEnoughAmountException if amount is not enough for the
     * withdraw.
     */
    public synchronized boolean withdraw(long aAmount, Runnable aOnWithdraw) throws NotEnoughAmountException {
        if (!abandoned) {
            if (amount >= aAmount) {
                amount -= aAmount;
                aOnWithdraw.run();
            } else {
                throw new NotEnoughAmountException(accountNumber);
            }
        }
        return abandoned;
    }

}
