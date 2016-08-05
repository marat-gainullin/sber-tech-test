package com.sbertech.accounts.client;

import com.sbertech.accounts.model.Account;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
     * @param aAccountsProcessor
     */
    public OperationsView(final Account aAccount, final AccountsProcessor aAccountsProcessor) {
        super();
        account = aAccount;
        accountProcessor = aAccountsProcessor;
        ResourceBundle res = ResourceBundle.getBundle(OperationsView.class.getPackage().getName() + ".Bundle");
        AnchorPane operationsPane = new AnchorPane();
        operationsTable = new TableView<>();
        TableColumn<Transfer, String> transferNumberColumn = new TableColumn<>(res.getString("transfer.number"));
        transferNumberColumn.setPrefWidth(100);
        transferNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Transfer, Date> transferCreatedColumn = new TableColumn<>(res.getString("transfer.created"));
        transferCreatedColumn.setPrefWidth(150);
        transferCreatedColumn.setCellFactory((TableColumn<Transfer, Date> aColumn) -> new TextFieldTableCell<>(new StringConverter<Date>() {

            private final DateFormat formatterParser = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");

            @Override
            public String toString(Date aValue) {
                return aValue != null ? formatterParser.format(aValue) : "";
            }

            @Override
            public Date fromString(String aText) {
                try {
                    return formatterParser.parse(aText);
                } catch (ParseException ex) {
                    Logger.getLogger(OperationsView.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }));
        transferCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        TableColumn<Transfer, Long> transferAmountColumn = new TableColumn<>(res.getString("transfer.amount"));
        transferAmountColumn.setPrefWidth(100);
        transferAmountColumn.setCellFactory((TableColumn<Transfer, Long> aColumn) -> new TextFieldTableCell<>(new StringConverter<Long>() {
            @Override
            public String toString(Long aValue) {
                return aValue != null ? "" + (aValue / 100) : "";
            }

            @Override
            public Long fromString(String aText) {
                return (long) (Double.valueOf(aText) * 100);
            }
        }));
        transferAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transfer, String> transferTypeColumn = new TableColumn<>(res.getString("transfer.type"));
        transferTypeColumn.setPrefWidth(100);
        transferTypeColumn.setCellValueFactory((TableColumn.CellDataFeatures<Transfer, String> aRow) -> {
            if (aAccount.getAccountNumber().equals(aRow.getValue().getFromAccount())) {
                return new ReadOnlyObjectWrapper<>(res.getString("transaction.type.withdraw"));
            } else {
                return new ReadOnlyObjectWrapper<>(res.getString("transaction.type.addition"));
            }
        });
        /*
        TableColumn<Transfer, Date> transferFromColumn = new TableColumn<>(res.getString("transfer.from"));
        transferFromColumn.setPrefWidth(200);
        transferFromColumn.setCellValueFactory(new PropertyValueFactory<>("fromAccount"));
        TableColumn<Transfer, Date> transferToColumn = new TableColumn<>(res.getString("transfer.to"));
        transferToColumn.setPrefWidth(200);
        transferToColumn.setCellValueFactory(new PropertyValueFactory<>("toAccount"));
         */

        operationsTable.getColumns().add(transferTypeColumn);
        operationsTable.getColumns().add(transferAmountColumn);
        operationsTable.getColumns().add(transferCreatedColumn);
        operationsTable.getColumns().add(transferNumberColumn);
        //operationsTable.getColumns().add(transferToColumn);

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

        setTitle(MessageFormat.format(res.getString("operations.view.title"), account.getAccountNumber()));
        setScene(new Scene(operationsPane, 472, 300));
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
