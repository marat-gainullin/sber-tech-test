package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.exceptions.EmptyTransferException;
import com.sbertech.accounts.exceptions.NotEnoughAmountException;
import com.sbertech.accounts.exceptions.SameAccountsTransferException;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

/**
 *
 * @author mg
 */
public class TransferView extends Stage {

    public TransferView(AccountsProcessor aProcessor, Account aSourceAccount, ObservableList<Account> aAccounts, BiConsumer<Account, Account> aOnTransfer) {
        super();
        ResourceBundle res = ResourceBundle.getBundle(SttApplication.class.getPackage().getName() + ".Bundle");
        ComboBox<Account> cmbFrom = new ComboBox<>();
        cmbFrom.setItems(aAccounts);
        StringConverter<Account> accountConverter = new StringConverter<Account>() {
            @Override
            public String toString(Account aAccount) {
                return MessageFormat.format("{0} ({1})", aAccount.getDescription(), aAccount.getAccountNumber());
            }

            @Override
            public Account fromString(String aManualDesc) {
                Optional<Account> found = aAccounts.stream().filter((Account aAccount) -> {
                    return aAccount.getDescription().contains(aManualDesc) || aAccount.getAccountNumber().contains(aManualDesc);
                }).findFirst();
                return found.isPresent() ? found.get() : null;
            }
        };
        cmbFrom.setConverter(accountConverter);
        cmbFrom.getSelectionModel().select(aSourceAccount);

        ComboBox<Account> cmbTo = new ComboBox<>();
        cmbTo.setItems(aAccounts);
        cmbTo.setConverter(accountConverter);

        TextField amountEditor = new TextField();
        amountEditor.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));

        HBox amountBox = new HBox(10d, new Label(res.getString("label.tranfer.amount.title")), amountEditor);
        Label lblFrom = new Label(res.getString("label.transfer.from.title"));
        lblFrom.setPrefWidth(70);
        Label lblTo = new Label(res.getString("label.transfer.to.title"));
        lblTo.setPrefWidth(70);
        HBox accountsBox = new HBox(10d, lblFrom, cmbFrom, lblTo, cmbTo);
        VBox transferBox = new VBox(10d, accountsBox, amountBox);
        AnchorPane transferPane = new AnchorPane();
        transferPane.getChildren().add(transferBox);

        Button btnOk = new Button(res.getString("button.ok.title"));
        transferPane.getChildren().add(btnOk);

        AnchorPane.setLeftAnchor(transferBox, 10d);
        AnchorPane.setTopAnchor(transferBox, 10d);
        AnchorPane.setRightAnchor(transferBox, 10d);
        AnchorPane.setBottomAnchor(transferBox, btnOk.getPrefHeight() + 20d);

        btnOk.setPrefWidth(70);
        btnOk.setPrefHeight(25);
        AnchorPane.setRightAnchor(btnOk, 10d);
        AnchorPane.setBottomAnchor(btnOk, 10d);

        btnOk.setOnAction(e -> {
            try {
                Account from = cmbFrom.getSelectionModel().getSelectedItem();
                Account to = cmbTo.getSelectionModel().getSelectedItem();
                if (from == null) {
                    Alert fromAbsent = new Alert(AlertType.ERROR, res.getString("alert.from.absent"));
                    fromAbsent.showAndWait();
                    return;
                }
                if (to == null) {
                    Alert toAbsent = new Alert(AlertType.ERROR, res.getString("alert.to.absent"));
                    toAbsent.showAndWait();
                    return;
                }
                if (from == to) {
                    Alert toFromSame = new Alert(AlertType.ERROR, res.getString("alert.to.from.same"));
                    toFromSame.showAndWait();
                    return;
                }
                if (amountEditor.getText().isEmpty()) {
                    Alert amountAbsent = new Alert(AlertType.ERROR, res.getString("alert.amount.absent"));
                    amountAbsent.showAndWait();
                    return;
                }
                final Double amount = Double.valueOf(amountEditor.getText()) * 100d;

                aProcessor.transfer(new Transfer(from.getAccountNumber(),
                        to.getAccountNumber(),
                        amount.longValue()
                ));
                TransferView.this.close();
                aOnTransfer.accept(from, to);
            } catch (NotEnoughAmountException | IOException | EmptyTransferException | SameAccountsTransferException ex) {
                Logger.getLogger(TransferView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        setTitle(res.getString("transfer.view.title"));
        setScene(new Scene(transferPane, 650, 250));
    }
}
