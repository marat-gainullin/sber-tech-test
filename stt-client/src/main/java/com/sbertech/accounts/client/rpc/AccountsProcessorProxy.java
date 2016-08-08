package com.sbertech.accounts.client.rpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.exceptions.NotEnoughAmountException;
import com.sbertech.accounts.model.Transfer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

/**
 * RPC proxy of {@code AccountsProcessor}.
 *
 * @author mg
 * @see AccountsProcessor
 */
public class AccountsProcessorProxy implements AccountsProcessor {

    private final String accountsUrl;
    private final String transfersUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountsProcessorProxy(String aAccountsUrl, String aTransfersUrl) {
        super();
        accountsUrl = aAccountsUrl;
        transfersUrl = aTransfersUrl;
    }

    @Override
    public void transfer(Transfer aTransfer) throws NotEnoughAmountException,
            IOException {
        String transferJson = mapper.writeValueAsString(aTransfer);
        URL destUrl = new URL(transfersUrl);
        HttpURLConnection connection = (HttpURLConnection) destUrl.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json;charset=UTF-8");
        try (OutputStream out = connection.getOutputStream()) {
            out.write(transferJson.getBytes("utf-8"));
        }
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                throw new NotEnoughAmountException(aTransfer.getFromAccount());
            } else {
                throw new IllegalStateException(MessageFormat.format("Unrecognized error code from server {0} : {1}", responseCode, connection.getResponseMessage()));
            }
        }
    }

    @Override
    public Collection<Transfer> transfersOnAccount(String aAccountNumber) throws IOException {
        return mapper.readValue(new URL(MessageFormat.format("{0}/{1}/operations", accountsUrl, aAccountNumber)), new TypeReference<List<Transfer>>(){});
    }

}
