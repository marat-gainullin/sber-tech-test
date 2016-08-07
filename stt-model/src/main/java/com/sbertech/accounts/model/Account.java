package com.sbertech.accounts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Account persistent POJO. It is persistent entity.
 *
 * @author mg
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "account.by.number", query = ""
            + " from Account a"
            + " where a.accountNumber = :accountNumber"),
    @NamedQuery(name = "accounts.all", query = ""
            + " from Account a order by a.accountNumber")})
public class Account implements Serializable {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 3471069051927286254L;

    /**
     * Account number. It is not final because of ORM.
     */
    @Id
    @Column
    private String accountNumber;

    /**
     * The account's amount. This value is per cent accurate. It is not final
     * because of ORM.
     */
    @Column
    private long amount;

    /**
     * Account's description. It is not final because of ORM.
     */
    @Column
    private String description;

    /**
     * Abandoned flag.
     */
    @JsonIgnore
    private transient boolean abandoned;

    /**
     * Account's no args constructor. Used by ORM.
     */
    public Account() {
        super();
    }

    /**
     * Amount getter. Amount is of per cent accuracy.
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
     * Add an amount to this account.
     *
     * @param aAmount Amount to add to this account.
     */
    public final void add(final long aAmount) {
        amount += aAmount;
    }

    /**
     * Abandoned flag getter.
     *
     * @return True if this account is already abandoned and false otherwise.
     */
    public final synchronized boolean abandoned() {
        return abandoned;
    }

    /**
     * Abandones the {@code Account} instance.
     */
    public final synchronized void abandone() {
        abandoned = true;
    }

}
