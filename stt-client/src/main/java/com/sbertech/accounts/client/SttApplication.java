package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Stt client entry point class. Initializes JavaFX and Spring and shows
 * accounts view.
 *
 * @author mg
 */
public class SttApplication extends Application {

    private static final String DEF_ACCOUNTS_URL = "http://localhost:8080/stt/accounts/";

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage aPrimaryStage) throws Exception {
        AccountsView av = new AccountsView(DEF_ACCOUNTS_URL);
        av.showAccounts();
    }
}
