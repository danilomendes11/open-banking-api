package unicamp.mc946.openbankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stellar.sdk.AssetTypeNative;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.service.AssetService;
import unicamp.mc946.openbankingapi.service.UserService;

@RestController()
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
            return user.getStellarAccId();
        } catch (Exception e) {
            return "Não foi possivel criar user";
        }
    }

    @GetMapping("/balance")
    public String getBalance(@RequestParam String login) {
        return userService.getBalance(login);
    }

    @PostMapping("/transaction")
    public String createTransaction(@RequestParam String login, @RequestParam String receiverAccId,
                                    @RequestParam Double amount, @RequestParam String assetName) {
        return userService.createTransaction(login, receiverAccId, amount, assetName);
    }

    @PostMapping("/issue")
    public String createAsset(@RequestParam String login, @RequestParam String name, @RequestParam Double amount){
        assetService.issueAssetForAccount(name, login, amount);
        return "Success";
    }

}
