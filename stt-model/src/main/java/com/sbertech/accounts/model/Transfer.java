package com.sbertech.accounts.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Transfer information POJO.
 *
 * @author mg
 */
@Entity
@NamedQuery(name = "transfers.byaccount",
        query = ""
        + " from Transfer tr"
        + " where "
        + "tr.fromAccount = :account or "
        + "tr.toAccount = :account")
public class Transfer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4660668591657719072L;

    /**
     * The transfer primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Account number from which the transfer is done.
     */
    private String fromAccount;

    /**
     * Account number to which the transfer is done.
     */
    private String toAccount;

    /**
     * The transfer amount.
     */
    private Long amount;

    /**
     * No args constructor for persistent layer.
     */
    public Transfer() {
        super();
        created = new Date();
    }

    /**
     * Four arguments constructor for in application logic.
     *
     * @param aFromAccount A source account number.
     * @param aToAccount A destination account number.
     * @param aAmount A amount of transfer.
     * @param aCreated A transfer creation timestamp.
     */
    public Transfer(final String aFromAccount,
            final String aToAccount,
            final long aAmount, final Date aCreated) {
        super();
        fromAccount = aFromAccount;
        toAccount = aToAccount;
        amount = aAmount;
        created = aCreated;
    }

    /**
     * From account id getter.
     *
     * @return Account id from the tranfer is done.
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * From account id setter.
     *
     * @param aValue An account id from the transfer will be done.
     */
    public void setFromAccount(String aValue) {
        fromAccount = aValue;
    }

    /**
     * To account id getter.
     *
     * @return Account id the tranfer is done to.
     */
    public String getToAccount() {
        return toAccount;
    }

    /**
     * To account id setter.
     *
     * @param aValue An account id the transfer will be done to.
     */
    public void setToAccount(String aValue) {
        toAccount = aValue;
    }

    /**
     * The transfer amount getter.
     *
     * @return the transfer amount.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * The transfer amount setter.
     *
     * @param aValue of the transfer.
     */
    public void setAmount(Long aValue) {
        amount = aValue;
    }

    /**
     * Transfer identifier getter.
     *
     * @return Transaction primary key.
     */
    public Long getId() {
        return id;
    }

    /**
     * The transfer identifier setter.
     *
     * @param aId The transfer identifier to be set.
     */
    public void setId(Long aId) {
        id = aId;
    }

    /**
     * Transfer timestamp getter.
     *
     * @return Transfer timestamp.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Transfer timestamp setter.
     *
     * @param aValue {@code Date} instance to be setted as transfer timestamp.
     */
    public void setCreated(Date aValue) {
        created = aValue;
    }

    /**
     * Copies this transfer and normalizes its data. Swaps from account and to
     * account with each other if amount is less then zero. Also created
     * property is filled up with current time.
     *
     * @return Normalized {@code Transfer} instance.
     */
    public Transfer normalize() {
        if (amount < 0) {
            return new Transfer(toAccount, fromAccount, -amount, new Date());
        } else {
            return new Transfer(fromAccount, toAccount, amount, new Date());
        }
    }
}
