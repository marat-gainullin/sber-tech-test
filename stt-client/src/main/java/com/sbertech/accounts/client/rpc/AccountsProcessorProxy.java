package com.sbertech.accounts.client.rpc;

import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.NotEnoughAmountException;
import com.sbertech.accounts.model.Transfer;
import java.util.Collection;
import org.springframework.scheduling.annotation.Async;

/**
 * RPC proxy of {@code AccountsProcessor}.
 *
 * @author mg
 * @see AccountsProcessor
 */
public class AccountsProcessorProxy implements AccountsProcessor {

    @Async
    @Override
    public void transfer(Transfer aTransfer) throws NotEnoughAmountException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Async
    @Override
    public Collection<Transfer> transfersOnAccount(String aAccountNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
