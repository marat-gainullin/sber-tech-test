package com.sbertech.accounts.client;

import java.text.MessageFormat;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Stt client entry point class. Initializes JavaFX and Spring and shows
 * accounts view.
 *
 * @author mg
 */
public class SttApplication extends Application {

    private static final String DEF_BASE_URL = "http://localhost:8080/stt/";

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage aPrimaryStage) throws Exception {
        String baseUrl = getParameters().getNamed()
                .getOrDefault("-url", DEF_BASE_URL);
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        AccountsView av = new AccountsView(MessageFormat.format("{0}/accounts", baseUrl),
                MessageFormat.format("{0}/transfers", baseUrl));
        av.showAccounts();
    }
}
