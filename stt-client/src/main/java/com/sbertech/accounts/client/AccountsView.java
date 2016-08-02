package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * User's view with accounts list.
 *
 * @author mg
 */
public class AccountsView extends Stage {

    public AccountsView() {
        super();
        ResourceBundle res = ResourceBundle.getBundle(SttApplication.class.getPackage().getName() + ".Bundle");

        TableView<Account> accountsTable = new TableView<>();

        TableColumn<Account, String> accountNumberColumn = new TableColumn<>(res.getString("account.number"));
        accountNumberColumn.setPrefWidth(200);
        TableColumn<Account, String> accountDescColumn = new TableColumn<>(res.getString("account.desc"));
        accountDescColumn.setPrefWidth(200);
        TableColumn<Account, Long> accountAmountColumn = new TableColumn<>(res.getString("account.amount"));
        accountAmountColumn.setPrefWidth(100);

        accountsTable.getColumns().add(accountNumberColumn);
        accountsTable.getColumns().add(accountDescColumn);
        accountsTable.getColumns().add(accountAmountColumn);

        AnchorPane accountsPane = new AnchorPane();
        accountsPane.getChildren().add(accountsTable);
        AnchorPane.setLeftAnchor(accountsTable, 10.0d);
        AnchorPane.setRightAnchor(accountsTable, 10.0d);
        AnchorPane.setTopAnchor(accountsTable, 10.0d);
        AnchorPane.setBottomAnchor(accountsTable, 50.0d);

        Button btnClose = new Button(res.getString("button.close.title"));
        btnClose.setPrefSize(70d, 25d);
        btnClose.setOnAction(e -> AccountsView.this.close());

        accountsPane.getChildren().add(btnClose);
        AnchorPane.setRightAnchor(btnClose, 10.0d);
        AnchorPane.setBottomAnchor(btnClose, 10.0d);

        Button btnOperations = new Button(res.getString("button.operations.title"));
        btnOperations.setPrefSize(100d, 25d);

        accountsPane.getChildren().add(btnOperations);
        AnchorPane.setLeftAnchor(btnOperations, 10.0d);
        AnchorPane.setTopAnchor(btnOperations, 10.0d);

        Button btnTransfer = new Button(res.getString("button.transfer.title"));
        btnTransfer.setPrefSize(100d, 25d);

        accountsPane.getChildren().add(btnTransfer);
        AnchorPane.setLeftAnchor(btnTransfer, 10.0d + AnchorPane.getLeftAnchor(btnOperations) + btnOperations.getPrefWidth());
        AnchorPane.setTopAnchor(btnTransfer, 10.0d);

        setScene(new Scene(accountsPane, 520, 400));
        setTitle(res.getString("accounts.view.title"));
    }

}
