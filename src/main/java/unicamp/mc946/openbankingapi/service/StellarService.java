package unicamp.mc946.openbankingapi.service;

import org.springframework.stereotype.Service;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.TransactionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@Service
public class StellarService {

    private final Server server;

    public StellarService() {
        server = new Server("https://horizon-testnet.stellar.org");
    }

    private KeyPair generateRandomKeyPair() {
        return KeyPair.random();
    }

    public KeyPair createStellarUser() {
        KeyPair pair = generateRandomKeyPair();
        String friendbotUrl = String.format(
                "https://friendbot.stellar.org/?addr=%s",
                pair.getAccountId());
        InputStream response = null;
        try {
            response = new URL(friendbotUrl).openStream();
            String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
            System.out.println(body);
            return pair;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createTransaction(String senderSecretToken, String receiverAccountId, Double amount, Asset asset) {
        KeyPair source = KeyPair.fromSecretSeed(senderSecretToken);
        KeyPair destination = KeyPair.fromAccountId(receiverAccountId);

        try {
            AccountResponse sourceAccount = server.accounts().account(source.getAccountId());

            Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET)
                    .addOperation(new PaymentOperation.Builder(destination.getAccountId(), asset, amount.toString()).build())
                    .addMemo(Memo.text("Test Transaction"))
                    .setTimeout(180)
                    .setBaseFee(100)
                    .build();
            transaction.sign(source);

            SubmitTransactionResponse response = server.submitTransaction(transaction);
            return String.valueOf(response.isSuccess());
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getBalancesForAccount(String accountId) {
        try {
            StringBuilder balances = new StringBuilder();
            AccountResponse account = server.accounts().account(accountId);
            balances.append(String.format("Saldo de %s%n", accountId));
            for (AccountResponse.Balance balance : account.getBalances()) {
                balances.append(String.format(
                        "Type: %s, Code: %s, Balance: %s%n",
                        balance.getAssetType(),
                        balance.getAssetCode(),
                        balance.getBalance()
                ));
            }
            return balances.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Asset issueAsset(String assetName, String issuerPrivateKey, Double value){
        try {
            KeyPair issuingKeys = KeyPair
                    .fromSecretSeed("SCQVH5BPLHELP3O3TLZDINJYTCEQCPEXGU4F7RMARACGUBZ6IL4SSE56");
            KeyPair receivingKeys = KeyPair
                    .fromSecretSeed(issuerPrivateKey);

            Asset asset = Asset.createNonNativeAsset(assetName, issuingKeys.getAccountId());

            AccountResponse receiving = server.accounts().account(receivingKeys.getAccountId());
            Transaction allowAsset = new Transaction.Builder(receiving, Network.TESTNET)
                    .addOperation(
                            new ChangeTrustOperation.Builder(asset, "1000000").build())
                    .setTimeout(180)
                    .setBaseFee(100)
                    .build();
            allowAsset.sign(receivingKeys);
            server.submitTransaction(allowAsset);

            AccountResponse issuing = server.accounts().account(issuingKeys.getAccountId());
            Transaction issueAsset = new Transaction.Builder(issuing, Network.TESTNET)
                    .addOperation(
                            new PaymentOperation.Builder(receivingKeys.getAccountId(), asset, value.toString()).build())
                    .setTimeout(180)
                    .setBaseFee(100)
                    .build();
            issueAsset.sign(issuingKeys);
            server.submitTransaction(issueAsset);

            return asset;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getTransactionsForAccount(String accId){
        StringBuilder balances = new StringBuilder();
        Page<TransactionResponse> responsePage = null;
        try {
            responsePage = server.transactions().forAccount(accId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (TransactionResponse resp: responsePage.getRecords()) {
            balances.append(String.format(
                    "source: %s, fee: %s, date: %s%n",
                    resp.getSourceAccount(),
                    resp.getFeeCharged(),
                    resp.getCreatedAt()
            ));
        }
        return balances.toString();
    }

}
