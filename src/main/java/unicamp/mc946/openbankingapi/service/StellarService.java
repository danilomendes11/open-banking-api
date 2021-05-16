package unicamp.mc946.openbankingapi.service;

import org.springframework.stereotype.Service;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;

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

    public void getBalancesForAccount(String accountId) {
        try {
            AccountResponse account = server.accounts().account(accountId);
            for (AccountResponse.Balance balance : account.getBalances()) {
                System.out.printf(
                        "Type: %s, Code: %s, Balance: %s%n",
                        balance.getAssetType(),
                        balance.getAssetCode(),
                        balance.getBalance()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
