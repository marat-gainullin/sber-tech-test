package com.sbertech.accounts.client;

import com.sbertech.accounts.client.rpc.AccountsProcessorProxy;
import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Account user's view. Shows account's operations.
 *
 * @author mg
 */
public class OperationsView extends Stage {

    /**
     * Account being viewed.
     */
    private final Account account;

    private final AccountsProcessor accountProcessor;

    private final TableView<Transfer> operationsTable;

    /**
     * {@code AccountView} constructor.
     *
     * @param aAccount An {@code Account} instance for the view.
     * @param aAccountsUrl
     */
    public OperationsView(Account aAccount, String aAccountsUrl) {
        super();
        account = aAccount;
        accountProcessor = new AccountsProcessorProxy(aAccountsUrl);
        ResourceBundle res = ResourceBundle.getBundle(OperationsView.class.getPackage().getName() + ".Bundle");
        AnchorPane operationsPane = new AnchorPane();
        operationsTable = new TableView<>();
        TableColumn<Transfer, String> transferNumberColumn = new TableColumn<>(res.getString("transfer.number"));
        transferNumberColumn.setPrefWidth(200);
        TableColumn<Transfer, Date> transferCreatedColumn = new TableColumn<>(res.getString("transfer.created"));
        transferCreatedColumn.setPrefWidth(150);
        TableColumn<Transfer, Long> transferAmountColumn = new TableColumn<>(res.getString("transfer.amount"));
        transferAmountColumn.setPrefWidth(100);
        TableColumn<Transfer, Date> transferFromColumn = new TableColumn<>(res.getString("transfer.from"));
        transferFromColumn.setPrefWidth(200);
        TableColumn<Transfer, Date> transferToColumn = new TableColumn<>(res.getString("transfer.to"));
        transferToColumn.setPrefWidth(200);

        operationsTable.getColumns().add(transferNumberColumn);
        operationsTable.getColumns().add(transferAmountColumn);
        operationsTable.getColumns().add(transferCreatedColumn);
        operationsTable.getColumns().add(transferFromColumn);
        operationsTable.getColumns().add(transferToColumn);

        operationsPane.getChildren().add(operationsTable);

        AnchorPane.setLeftAnchor(operationsTable, 10.0d);
        AnchorPane.setRightAnchor(operationsTable, 10.0d);
        AnchorPane.setTopAnchor(operationsTable, 90.0d);
        AnchorPane.setBottomAnchor(operationsTable, 50.0d);

        Button btnClose = new Button(res.getString("button.close.title"));
        btnClose.setPrefSize(70d, 25d);
        btnClose.setOnAction(e -> OperationsView.this.close());

        operationsPane.getChildren().add(btnClose);
        AnchorPane.setRightAnchor(btnClose, 10.0d);
        AnchorPane.setBottomAnchor(btnClose, 10.0d);

        HBox accountNumberBox = new HBox(10, new Label(res.getString("label.account.number.title")), new Label(account.getAccountNumber()));
        HBox accountAmountBox = new HBox(10, new Label(res.getString("label.account.amount.title")), new Label("" + account.getAmount() / 100d));
        VBox accountBox = new VBox(10, accountNumberBox, accountAmountBox, new Label(account.getDescription()));

        operationsPane.getChildren().add(accountBox);
        AnchorPane.setTopAnchor(accountBox, 10.0d);
        AnchorPane.setLeftAnchor(accountBox, 10.0d);
        AnchorPane.setRightAnchor(accountBox, 10.0d);

        setScene(new Scene(operationsPane, 870, 400));
        setTitle(MessageFormat.format(res.getString("operations.view.title"), account.getAccountNumber()));
    }

    /**
     * Account getter.
     *
     * @return {@code Account} instance of the view.
     */
    public Account getAccount() {
        return account;
    }

    public void showOperations() throws IOException {
        show();
        Collection<Transfer> transfers = accountProcessor.transfersOnAccount(account.getAccountNumber());
        operationsTable.getItems().addAll(transfers);
    }
}
