package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import java.text.MessageFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Account use's view. Shows account's operations.
 *
 * @author mg
 */
public class OperationsView extends Stage {

    /**
     * Account being viewed.
     */
    private final Account account;

    /**
     * {@code AccountView} constructor.
     *
     * @param aAccount An {@code Account} instance for the view.
     */
    public OperationsView(Account aAccount) {
        super();
        account = aAccount;
        ResourceBundle res = ResourceBundle.getBundle(OperationsView.class.getPackage().getName() + ".Bundle");
        AnchorPane operationsPane = new AnchorPane();
        TableView<Account> operationsTable = new TableView<>();
        TableColumn<Account, String> transferNumberColumn = new TableColumn<>(res.getString("transfer.number"));
        transferNumberColumn.setPrefWidth(200);
        TableColumn<Account, Date> transferCreatedColumn = new TableColumn<>(res.getString("transfer.created"));
        transferCreatedColumn.setPrefWidth(150);
        TableColumn<Account, Long> transferAmountColumn = new TableColumn<>(res.getString("transfer.amount"));
        transferAmountColumn.setPrefWidth(100);
        TableColumn<Account, Date> transferFromColumn = new TableColumn<>(res.getString("transfer.from"));
        transferFromColumn.setPrefWidth(200);
        TableColumn<Account, Date> transferToColumn = new TableColumn<>(res.getString("transfer.to"));
        transferToColumn.setPrefWidth(200);

        operationsTable.getColumns().add(transferNumberColumn);
        operationsTable.getColumns().add(transferAmountColumn);
        operationsTable.getColumns().add(transferCreatedColumn);
        operationsTable.getColumns().add(transferFromColumn);
        operationsTable.getColumns().add(transferToColumn);

        operationsPane.getChildren().add(operationsTable);

        AnchorPane.setLeftAnchor(operationsTable, 10.0d);
        AnchorPane.setRightAnchor(operationsTable, 10.0d);
        AnchorPane.setTopAnchor(operationsTable, 50.0d);
        AnchorPane.setBottomAnchor(operationsTable, 50.0d);

        Button btnClose = new Button(res.getString("button.close.title"));
        btnClose.setPrefSize(70d, 25d);
        btnClose.setOnAction(e -> OperationsView.this.close());

        operationsPane.getChildren().add(btnClose);
        AnchorPane.setRightAnchor(btnClose, 10.0d);
        AnchorPane.setBottomAnchor(btnClose, 10.0d);

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

}
