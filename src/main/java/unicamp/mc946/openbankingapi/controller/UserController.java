package unicamp.mc946.openbankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.service.StellarService;
import unicamp.mc946.openbankingapi.service.UserService;

@RestController()
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StellarService stellarService;

    @GetMapping("/")
    public String testEndpoint(){
        return "Sucesso";
    }

    @GetMapping("/create")
    public String createUser(@RequestParam String name, @RequestParam String login, @RequestParam String password) {
        try{
            User user = userService.createUser(name, login, password);
            return user.getStellarAccId();
        }catch (Exception e){
            return "Não foi possivel criar user";
        }
    }


}
