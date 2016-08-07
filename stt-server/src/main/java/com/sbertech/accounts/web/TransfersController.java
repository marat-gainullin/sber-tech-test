package com.sbertech.accounts.web;

import com.sbertech.accounts.exceptions.BadTransferException;
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
import com.sbertech.accounts.exceptions.NotEnoughAmountException;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.exceptions.EmptyTransferException;
import com.sbertech.accounts.exceptions.SameAccountsTransferException;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import com.sbertech.accounts.model.TransfersStore;
import javax.persistence.NoResultException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * JSON REST end point of transfers API.
 *
 * @author mg
 */
@RestController
public class TransfersController {

    /**
     * Transfers store.
     */
    @Autowired
    private TransfersStore transfersStore;

    /**
     * Accounts processor.
     */
    @Autowired
    private AccountsProcessor processor;

    /**
     * Creates a transfer between accounts.
     *
     * @param aTransfer A JSON binded transfer body.
     * @param aResponse Http servlet response. Used to send 'location' header.
     * @throws NotEnoughAmountException if a source account has no enough amount
     * to make the transfer.
     * @throws IOException if some error while communications occur.
     * @throws BadTransferException if some data of a transfer is absent.
     * @throws EmptyTransferException if {@code aTransfer} has zero amount.
     * @throws SameAccountsTransferException when an attempt of transfer from an
     * account to itself is made.
     */
    @RequestMapping(path = "transfers",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public final void makeTransfer(@RequestBody final Transfer aTransfer,
            final HttpServletResponse aResponse)
            throws
            NotEnoughAmountException,
            IOException,
            BadTransferException,
            EmptyTransferException,
            SameAccountsTransferException {
        if (aTransfer.getAmount() == null) {
            throw new BadTransferException("'Amount' is null");
        }
        if (aTransfer.getFromAccount() == null) {
            throw new BadTransferException("'From' account number is null");
        }
        if (aTransfer.getToAccount() == null) {
            throw new BadTransferException("'To' account number is null");
        }
        Transfer transfer = aTransfer.normalize();
        processor.transfer(transfer);
        aResponse.setHeader(HttpHeaders.LOCATION,
                MessageFormat.format("/transfers/{0}", transfer.getId()));
    }

    /**
     * Handles {@code NotEnoughAmountException} exceptions.
     *
     * @param ex A {@code NotEnoughAmountException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see NotEnoughAmountException
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotEnoughAmountException.class)
    public final String handleNotEnoughAmount(
            final NotEnoughAmountException ex) {
        return ex.getMessage();
    }

    /**
     * Handles {@code EmptyTransferException} exceptions.
     *
     * @param ex A {@code EmptyTransferException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see EmptyTransferException
     */
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(EmptyTransferException.class)
    public final String handleEmptyTransfer(final EmptyTransferException ex) {
        return ex.getMessage();
    }

    /**
     * Handles {@code SameAccountsTransferException} exceptions.
     *
     * @param ex A {@code SameAccountsTransferException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see SameAccountsTransferException
     */
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(SameAccountsTransferException.class)
    public final String handleSameAccountTransfer(
            final SameAccountsTransferException ex) {
        return ex.getMessage();
    }

    /**
     * Handles {@code NoResultException} exceptions.
     *
     * @param ex A {@code NoResultException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see NoResultException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResultException.class)
    public final String handleNoResult(final NoResultException ex) {
        return ex.getMessage();
    }

    /**
     * Handles {@code BadTransferException} exceptions.
     *
     * @param ex A {@code BadTransferException} instance.
     * @return A {@code String} to be binded as graceful http response.
     * @see BadTransferException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadTransferException.class)
    public final String handleBadTransfer(final BadTransferException ex) {
        return ex.getMessage();
    }

    /**
     * Fetches transfers of adding to or withdraw from an account.
     *
     * @param aAccountNumber Number of an account, transfers will be fetched
     * for.
     * @return Collection of {@code Transfer} instances.
     * @throws IOException if some error while communications occur.
     */
    @RequestMapping(path = "accounts/{account-number}/operations",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Collection<Transfer> transfersFromToAccount(
            @PathVariable("account-number") final String aAccountNumber)
            throws IOException {
        return processor.transfersOnAccount(aAccountNumber);
    }

    /**
     * Looks up a transfer by its id.
     *
     * @param aTransferId A transfer id.
     * @return {@code Transfer} instance found.
     */
    @RequestMapping(value = "transfers/{transfer-id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public final Transfer transferById(
            @PathVariable("transfer-id") final Long aTransferId) {
        return transfersStore.find(aTransferId);
    }

}
