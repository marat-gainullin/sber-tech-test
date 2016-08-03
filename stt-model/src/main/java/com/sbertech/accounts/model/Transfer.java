package com.sbertech.accounts.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
        + "tr.fromAccountNumber = :accountNumber or "
        + "tr.toAccountNumber = :accountNumber")
public class Transfer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4660668591657719072L;

    /**
     * The transfer primary key.
     */
    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Account number from wich the transfer is done.
     */
    @ManyToOne(targetEntity = Account.class)
    private String fromAccountNumber;

    /**
     * Account number to wich the transfer is done.
     */
    @ManyToOne(targetEntity = Account.class)
    private String toAccountNumber;

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
     * Three args constructor for in application logic.
     *
     * @param aFromAccountNumber A source account number.
     * @param aToAccountNumber A destination account number.
     * @param aAmount A amount of transfer.
     * @param aCreated A transfer creation timestamp.
     */
    public Transfer(final String aFromAccountNumber,
            final String aToAccountNumber,
            final long aAmount, final Date aCreated) {
        super();
        fromAccountNumber = aFromAccountNumber;
        toAccountNumber = aToAccountNumber;
        created = aCreated;
    }

    /**
     * From account id getter.
     *
     * @return Account id from the tranfer is done.
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * From account id setter.
     *
     * @param aValue An account id from the transfer will be done.
     */
    public void setFromAccountNumber(String aValue) {
        fromAccountNumber = aValue;
    }

    /**
     * To account id getter.
     *
     * @return Account id the tranfer is done to.
     */
    public String getToAccountNumber() {
        return toAccountNumber;
    }

    /**
     * To account id setter.
     *
     * @param aValue An account id the transfer will be done to.
     */
    public void setToAccountNumber(String aValue) {
        toAccountNumber = aValue;
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
    public String getId() {
        return id;
    }

    /**
     * The transfer identifier setter.
     *
     * @param aId The transfer identifier to be set.
     */
    public void setId(String aId) {
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

}
