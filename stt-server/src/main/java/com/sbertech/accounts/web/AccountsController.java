package com.sbertech.accounts.web;

import java.text.MessageFormat;
import java.util.Collection;
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
import java.io.IOException;
import com.sbertech.accounts.model.TransfersStore;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    private TransfersStore operationsStore;

    /**
     * Accounts processor.
     */
    @Autowired
    private AccountsProcessor processor;

    /**
     * Retrieves a accounts list.
     *
     * @return JSON binded collection of accounts.
     * @throws IOException if some error while communications occur.
     */
    @RequestMapping(path = "accounts",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public final Collection<Account> accounts() throws IOException {
        return accountsStore.accounts();
    }

    /**
     * Looks up an account by its number.
     *
     * @param aAccountNumber Number of an account to lookup.
     * @return An {@code Account} instance got from repository.
     * @throws IOException if some error while communications occur.
     */
    @RequestMapping(path = "accounts/{account-number}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Account accountByNumber(
            @PathVariable("account-number") String aAccountNumber) throws IOException {
        return accountsStore.find(aAccountNumber);
    }

    /**
     * Creates an operation of transfer between accounts.
     *
     * @param aTransfer A JSON binded operation body.
     * @param aResponse Http servlet response. Used to send 'location' header.
     * @throws NotEnoughAmountException
     * @throws IOException if some error while communications occur.
     */
    @RequestMapping(path = "operations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public final void createTransfer(@RequestBody final Transfer aTransfer,
            HttpServletResponse aResponse) throws NotEnoughAmountException,
            IOException {
        Transfer transfer = aTransfer.normalize();
        processor.transfer(transfer);
        aResponse.setHeader(HttpHeaders.LOCATION,
                MessageFormat.format("/operations/{0}", transfer.getId()));
    }

    /**
     * Handles {@code NotEnoughAmountException} exceptions.
     *
     * @param ex A {@code NotEnoughAmountException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see TransactionNotFoundException
     * @see TransactionResult
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotEnoughAmountException.class)
    public final String handleNotEnoughAmount(final NotEnoughAmountException ex) {
        return ex.getMessage();
    }

    /**
     * Fetches operations of adding to or withdraw from an account.
     *
     * @param aAccountNumber Number of an account, operations will be fetched
     * for.
     * @return Collection of {@code Transfer} instances.
     * @throws IOException if some error while communications occur.
     */
    @RequestMapping(path = "accounts/{account-number}/operations",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Collection<Transfer> operationsOfAccount(
            @PathVariable("account-number") String aAccountNumber) throws IOException {
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
            @PathVariable("operation-id") Long anOperationId) {
        return operationsStore.find(anOperationId);
    }

}
