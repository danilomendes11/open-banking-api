package unicamp.mc946.openbankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.stellar.sdk.AssetTypeNative;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.service.AssetService;
import unicamp.mc946.openbankingapi.service.UserService;

@RestController()
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @GetMapping("/")
    public String healthcheck(){
        return "Sucesso";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String name, @RequestParam String login, @RequestParam String password) {
        try {
            User user = userService.createUser(name, login, password);
            return String.format("Usuário criado com sucesso. Número da conta: %s",user.getStellarAccId());
        } catch (Exception e) {
            return "Não foi possivel criar user";
        }
    }

    @GetMapping("/balance")
    public String getBalance(@RequestParam String login) {
        return userService.getBalance(login);
    }

    @GetMapping("/transactions")
    public String getTransactions(@RequestParam String login) {
        return userService.getTransactions(login);
    }

    @PostMapping("/transaction")
    public String createTransaction(@RequestParam String login, @RequestParam String receiverAccId,
                                    @RequestParam String amount, @RequestParam String assetName) {
        return userService.createTransaction(login, receiverAccId, amount, assetName);
    }

    @PostMapping("/issue")
    public String createAsset(@RequestParam String login, @RequestParam String name, @RequestParam String amount){
        assetService.issueAssetForAccount(name, login, Double.valueOf(amount));
        return "Moeda com sucesso";
    }

}
