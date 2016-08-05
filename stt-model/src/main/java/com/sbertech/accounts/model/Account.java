package com.sbertech.accounts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
     * Account number.
     */
    @Id
    private String accountNumber;

    /**
     * The account's amount. This value is per cent accurate.
     */
    private long amount;

    /**
     * Account's description.
     */
    private String description;

    /**
     * Abandoned flags
     */
    @JsonIgnore
    private transient boolean abandoned;

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
     * Add an amount to this account.
     *
     * @param aAmount Amount to add to this account.
     */
    public void add(long aAmount) {
        amount += aAmount;
    }

    public boolean abandoned() {
        return abandoned;
    }

    public synchronized void abandone() {
        abandoned = true;
    }

}
