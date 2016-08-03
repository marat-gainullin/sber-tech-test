package com.sbertech.accounts.client.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.NotEnoughAmountException;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * RPC proxy of {@code AccountsProcessor}.
 *
 * @author mg
 * @see AccountsProcessor
 */
public class AccountsProcessorProxy implements AccountsProcessor {

    private static class TransfersList extends ArrayList<Transfer> {

        public TransfersList() {
            super();
        }
    }

    private final String accountsUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountsProcessorProxy(String aAccountsUrl) {
        super();
        accountsUrl = aAccountsUrl;
    }

    @Override
    public void transfer(Transfer aTransfer) throws NotEnoughAmountException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Transfer> transfersOnAccount(String aAccountNumber) throws IOException {
        return mapper.readValue(new URL(accountsUrl + aAccountNumber + "/operations"), TransfersList.class);
    }

}
