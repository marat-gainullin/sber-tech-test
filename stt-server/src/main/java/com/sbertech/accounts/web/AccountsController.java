package com.sbertech.accounts.web;

import java.text.MessageFormat;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.sbertech.accounts.model.NotEnoughAmountException;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.model.Transfer;
import com.sbertech.accounts.model.OperationsStore;

/**
 * JSON REST end point of accounts API.
 *
 * @author mg
 */
@RestController
public class AccountsController {

    /**
     * Accounts store.
     */
    @Autowired
    private AccountsStore accountsStore;

    /**
     * Operations store.
     */
    @Autowired
    private OperationsStore operationsStore;

    /**
     * Accounts processor.
     */
    @Autowired
    private AccountsProcessor processor;

    /**
     * Creates an account.
     *
     * @param aAccount A JSON binded account body.
     * @param aRequest Http servlet request. Used for 'location' header
     * calculation.
     * @param aResponse Http servlet response. Used to send 'location' header.
     */
    @RequestMapping(path = "accounts",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public final void createAccount(@RequestBody final Account aAccount, HttpServletRequest aRequest, HttpServletResponse aResponse) {
        accountsStore.add(aAccount);
        aResponse.setHeader(HttpHeaders.LOCATION,
                MessageFormat.format("{0}/{1}", aRequest.getRequestURL(), aAccount.getAccountNumber()));
    }

    /**
     * Looks up an account by its number.
     *
     * @param aAccountNumber Number of an account to lookup.
     * @return
     */
    @RequestMapping(path = "accounts/{account-number}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Account accountByNumber(
            @PathVariable("account-number") String aAccountNumber) {
        return accountsStore.find(aAccountNumber);
    }

    /**
     * Creates an operation of transfer between accounts.
     *
     * @param aTransfer A JSON binded operation body.
     * @param aResponse Http servlet response. Used to send 'location' header.
     * @throws NotEnoughAmountException
     */
    @RequestMapping(path = "operations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public final void createOperation(@RequestBody final Transfer aTransfer,
            HttpServletResponse aResponse) throws NotEnoughAmountException {
        processor.transfer(aTransfer);
        aResponse.setHeader(HttpHeaders.LOCATION,
                MessageFormat.format("operations/{0}", aTransfer.getId()));
    }

    /**
     * Fetches operations of adding to or withdraw from an account.
     *
     * @param aAccountNumber Number of an account, operations will be fetched
     * for.
     * @return Collection of {@code Transfer} instances.
     */
    @RequestMapping(path = "accounts/{account-number}/operations",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Collection<Transfer> operationsOfAccount(
            @PathVariable("account-number") String aAccountNumber) {
        return processor.transfersOnAccount(aAccountNumber);
    }

    /**
     * Looks up a transfer operation by its id.
     *
     * @param anOperationId An operation id.
     * @return {@code Transfer} instance found.
     */
    @RequestMapping(value = "operations/{operation-id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Transfer operationById(
            @PathVariable("operation-id") String anOperationId) {
        return operationsStore.find(anOperationId);
    }

}
