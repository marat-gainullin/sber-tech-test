package com.sbertech.accounts.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Transfer information POJO.
 *
 * @author mg
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "transfer.by.id",
            query = ""
            + " from Transfer tr"
            + " where "
            + "tr.id = :id"),
    @NamedQuery(name = "transfers.byaccount",
            query = ""
            + " from Transfer tr"
            + " where "
            + "tr.fromAccount = :account or "
            + "tr.toAccount = :account")
})
public class Transfer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4660668591657719072L;

    /**
     * The transfer primary key. It is not final because of ORM.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    /**
     * The transfer creation timestamp. It is not final because of ORM.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date created;

    /**
     * Account number from which the transfer is done. It is not final because
     * of ORM.
     */
    @Column
    private String fromAccount;

    /**
     * Account number to which the transfer is done. It is not final because of
     * ORM.
     */
    @Column
    private String toAccount;

    /**
     * The transfer amount. It is not final because of ORM.
     */
    @Column
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
     */
    public Transfer(final String aFromAccount,
            final String aToAccount,
            final long aAmount) {
        super();
        fromAccount = aFromAccount;
        toAccount = aToAccount;
        amount = aAmount;
        created = new Date();
    }

    /**
     * From account id getter. It is not final because of ORM.
     *
     * @return Account id from the tranfer is done.
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * To account id getter. It is not final because of ORM.
     *
     * @return Account id the tranfer is done to.
     */
    public String getToAccount() {
        return toAccount;
    }

    /**
     * The transfer amount getter. It is not final because of ORM.
     *
     * @return the transfer amount.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Transfer identifier getter. It is not final because of ORM.
     *
     * @return Transaction primary key.
     */
    public Long getId() {
        return id;
    }

    /**
     * Transfer timestamp getter. It is not final because of ORM.
     *
     * @return Transfer timestamp.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Copies this transfer and normalizes its data. Swaps from account and to
     * account with each other if amount is less then zero. Also 'created'
     * property is filled up with current time.
     *
     * @return A normalized {@code Transfer} instance.
     */
    public final Transfer normalize() {
        if (amount < 0) {
            return new Transfer(toAccount, fromAccount, -amount);
        } else {
            return new Transfer(fromAccount, toAccount, amount);
        }
    }
}
