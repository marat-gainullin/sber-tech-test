package com.sbertech.accounts.client.rpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsStore;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

/**
 * RPC proxy of {@code AccountsStore}.
 *
 * @author mg
 * @see AccountsStore
 */
public class AccountsStoreProxy implements AccountsStore {

    private final String accountsUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountsStoreProxy(final String aAccountUrl) {
        super();
        accountsUrl = aAccountUrl;
    }

    @Override
    public Account find(String aAccountNumber) throws IOException {
        return mapper.readValue(new URL(MessageFormat.format("{0}/{1}", accountsUrl, aAccountNumber)), Account.class);
    }

    @Override
    public Collection<Account> accounts() throws IOException {
        return mapper.readValue(new URL(accountsUrl), new TypeReference<List<Account>>(){});
    }

}
