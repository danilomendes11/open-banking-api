package unicamp.mc946.openbankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.service.UserService;

@RestController()
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String healthcheck(){
        return "Sucesso";
    }

    @GetMapping("/create")
    public String createUser(@RequestParam String name, @RequestParam String login, @RequestParam String password) {
        try{
            User user = userService.createUser(name, login, password);
            return user.getStellarAccId();
        } catch (Exception e){
            return "Não foi possivel criar user";
        }
    }

    @GetMapping("/balance")
    public String getBalance(@RequestParam String login) {
        return userService.getBalance(login);
    }

    @GetMapping("/transaction")
    public String createTransaction(@RequestParam String login, @RequestParam String receiverAccId, @RequestParam Double amount) {
        return userService.createTransaction(login, receiverAccId, amount);
    }

}
