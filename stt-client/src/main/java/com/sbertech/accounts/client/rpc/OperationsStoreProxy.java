package com.sbertech.accounts.client.rpc;

import com.sbertech.accounts.model.OperationsStore;
import com.sbertech.accounts.model.Transfer;

/**
 * RPC proxy of {@code OperationsStore}.
 *
 * @author mg
 * @see OperationsStore
 */
public class OperationsStoreProxy implements OperationsStore {

    @Override
    public Transfer find(String aTransferId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTransfer(Transfer aTransfer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
