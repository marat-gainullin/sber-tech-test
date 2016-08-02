package com.sbertech.accounts.client.rpc;

import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsStore;
import org.springframework.scheduling.annotation.Async;

/**
 * RPC proxy of {@code AccountsStore}.
 *
 * @author mg
 * @see AccountsStore
 */
public class AccountsStoreProxy implements AccountsStore {

    @Async
    @Override
    public void add(Account aAccount) {
    }

    @Async
    @Override
    public Account find(String aAccountNumber) {
        return null;
    }

}
