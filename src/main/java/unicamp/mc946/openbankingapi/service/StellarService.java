package unicamp.mc946.openbankingapi.service;

import org.springframework.stereotype.Service;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

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
            System.out.println("SUCCESS! You have a new account :)\n" + body);
            return pair;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createTransaction(String senderSecretToken, String receiverAccountId, Double amount) {
        KeyPair source = KeyPair.fromSecretSeed(senderSecretToken);
        KeyPair destination = KeyPair.fromAccountId(receiverAccountId);

        // First, check to make sure that the destination account exists.
        // You could skip this, but if the account does not exist, you will be charged
        // the transaction fee when the transaction fails.
        // It will throw HttpResponseException if account does not exist or there was another error.
        try {
            server.accounts().account(destination.getAccountId());
            AccountResponse sourceAccount = server.accounts().account(source.getAccountId());

            // If there was no error, load up-to-date information on your account.

            // Start building the transaction.
            Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET)
                    .addOperation(new PaymentOperation.Builder(destination.getAccountId(), new AssetTypeNative(), amount.toString()).build())
                    // A memo allows you to add your own metadata to a transaction. It's
                    // optional and does not affect how Stellar treats the transaction.
                    .addMemo(Memo.text("Test Transaction"))
                    // Wait a maximum of three minutes for the transaction
                    .setTimeout(180)
                    .setBaseFee(100)
                    .build();
            // Sign the transaction to prove you are actually the person sending it.
            transaction.sign(source);

            SubmitTransactionResponse response = server.submitTransaction(transaction);
            return response.toString();
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

    public Asset createAsset(String name, String issuerAccId){
        Asset asset = Asset.createNonNativeAsset(name, issuerAccId);
        return asset;
    }

}
