/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sbertech.accounts;

import com.sbertech.accounts.SttConfig;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.model.TransfersStore;
import com.sbertech.accounts.services.AccountsDatabaseStoreBean;
import com.sbertech.accounts.services.AccountsProcessorBean;
import com.sbertech.accounts.services.TransfersDatabaseStoreBean;
import com.sbertech.accounts.web.AccountsController;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author MGaynullin
 */
public class AccountsTestConfig extends SttConfig {

    @Bean
    public AccountsProcessor accountsProcessor() {
        return new AccountsProcessorBean();
    }

    @Bean
    public AccountsStore accountsStore() {
        return new AccountsDatabaseStoreBean();
    }

    @Bean
    public TransfersStore transfersStore() {
        return new TransfersDatabaseStoreBean();
    }
    
    @Bean
    public AccountsController accountsController() {
        return new AccountsController();
    }
    
}
