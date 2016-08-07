package com.sbertech.accounts.web;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsStore;
import java.io.IOException;
import javax.persistence.NoResultException;
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
            @PathVariable("account-number") final String aAccountNumber)
            throws IOException {
        return accountsStore.find(aAccountNumber);
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
}
