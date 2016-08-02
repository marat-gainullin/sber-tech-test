package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Stt client entry point class. Initializes JavaFX and Spring and shows
 * accounts view.
 *
 * @author mg
 */
@SpringBootApplication
@EnableAsync
public class SttApplication extends Application {

    public static void main(String args[]) {
        SpringApplication.run(SttApplication.class);
        launch(args);
    }

    @Override
    public void start(Stage aPrimaryStage) throws Exception {
        Stage av = new AccountsView();
        av.show();
        Stage ov = new OperationsView(new Account());
        ov.show();
    }
}
