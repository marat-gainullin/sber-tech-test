package com.sbertech.accounts.client;

import com.sbertech.accounts.client.rpc.AccountsProcessorProxy;
import com.sbertech.accounts.client.rpc.AccountsStoreProxy;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.AccountsStore;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * User's view with accounts list.
 *
 * @author mg
 */
public class AccountsView extends Stage {

    private final AccountsStore store;
    private final AccountsProcessor processor;
    private final TableView<Account> accountsTable;

    public AccountsView(final String aAccountsUrl, final String aOperationsUrl) {
        super();
        store = new AccountsStoreProxy(aAccountsUrl);
        processor = new AccountsProcessorProxy(aAccountsUrl, aOperationsUrl);

        ResourceBundle res = ResourceBundle.getBundle(SttApplication.class.getPackage().getName() + ".Bundle");

        accountsTable = new TableView<>();

        accountsTable.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                showSelectedAccountOperations();
            }
        });

        TableColumn<Account, String> accountNumberColumn = new TableColumn<>(res.getString("account.number"));
        accountNumberColumn.setPrefWidth(200);
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        TableColumn<Account, String> accountDescColumn = new TableColumn<>(res.getString("account.desc"));
        accountDescColumn.setPrefWidth(200);
        accountDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Account, Long> accountAmountColumn = new TableColumn<>(res.getString("account.amount"));
        accountAmountColumn.setPrefWidth(100);
        accountAmountColumn.setCellFactory((TableColumn<Account, Long> aColumn) -> new TextFieldTableCell<>(new StringConverter<Long>() {
            @Override
            public String toString(Long aValue) {
                return aValue != null ? "" + (aValue / 100) : "";
            }

            @Override
            public Long fromString(String aText) {
                return (long) (Double.valueOf(aText) * 100);
            }
        }));
        accountAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        accountsTable.getColumns().add(accountDescColumn);
        accountsTable.getColumns().add(accountAmountColumn);
        accountsTable.getColumns().add(accountNumberColumn);

        AnchorPane accountsPane = new AnchorPane();
        accountsPane.getChildren().add(accountsTable);
        AnchorPane.setLeftAnchor(accountsTable, 10.0d);
        AnchorPane.setRightAnchor(accountsTable, 10.0d);
        AnchorPane.setTopAnchor(accountsTable, 50.0d);
        AnchorPane.setBottomAnchor(accountsTable, 50.0d);
        accountsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button btnClose = new Button(res.getString("button.close.title"));
        btnClose.setPrefSize(70d, 25d);
        btnClose.setOnAction(e -> AccountsView.this.close());

        accountsPane.getChildren().add(btnClose);
        AnchorPane.setRightAnchor(btnClose, 10.0d);
        AnchorPane.setBottomAnchor(btnClose, 10.0d);

        Button btnOperations = new Button(res.getString("button.operations.title"));
        btnOperations.setPrefSize(100d, 25d);
        btnOperations.setOnAction((e) -> {
            showSelectedAccountOperations();
        });

        accountsPane.getChildren().add(btnOperations);
        AnchorPane.setLeftAnchor(btnOperations, 10.0d);
        AnchorPane.setTopAnchor(btnOperations, 10.0d);

        Button btnTransfer = new Button(res.getString("button.transfer.title"));
        btnTransfer.setPrefSize(100d, 25d);
        btnTransfer.setOnAction((e) -> {
            TransferView tv = new TransferView(processor,
                    accountsTable.getSelectionModel().getSelectedItem(),
                    accountsTable.getItems(), (Account from, Account to) -> {
                        try {
                            int fromIdx = accountsTable.getItems().indexOf(from);
                            Account newFrom = store.find(from.getAccountNumber());
                            accountsTable.getItems().set(fromIdx, newFrom);

                            int toIdx = accountsTable.getItems().indexOf(to);
                            Account newTo = store.find(to.getAccountNumber());
                            accountsTable.getItems().set(toIdx, newTo);

                            accountsTable.getSelectionModel().select(newFrom);
                        } catch (IOException ex) {
                            Logger.getLogger(AccountsView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            tv.showAndWait();
        });

        accountsPane.getChildren().add(btnTransfer);
        AnchorPane.setLeftAnchor(btnTransfer, 10.0d + AnchorPane.getLeftAnchor(btnOperations) + btnOperations.getPrefWidth());
        AnchorPane.setTopAnchor(btnTransfer, 10.0d);

        setTitle(res.getString("accounts.view.title"));
        setScene(new Scene(accountsPane, 522, 400));
    }

    private void showSelectedAccountOperations() {
        try {
            Account selected = accountsTable.getSelectionModel().getSelectedItem();
            OperationsView view = new OperationsView(selected, processor);
            view.showOperations();
        } catch (IOException ex) {
            Logger.getLogger(AccountsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showAccounts() throws IOException {
        show();
        accountsTable.getItems().addAll(store.accounts());
        if (!accountsTable.getItems().isEmpty()) {
            accountsTable.getSelectionModel().select(accountsTable.getItems().get(0));
        }
    }
}
