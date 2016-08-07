package com.sbertech.accounts;

import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import com.sbertech.accounts.model.TransfersStore;
import com.sbertech.accounts.services.AccountsDatabaseStoreBean;
import com.sbertech.accounts.services.AccountsProcessorBean;
import com.sbertech.accounts.services.TransfersDatabaseStoreBean;
import com.sbertech.accounts.web.AccountsController;
import com.sbertech.accounts.web.TransfersController;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author mg
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
    public AccountsController accounts() {
        return new AccountsController();
    }

    @Bean
    public TransfersController transfers() {
        return new TransfersController();
    }

}
