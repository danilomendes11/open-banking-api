package unicamp.mc946.openbankingapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UserController {

    @GetMapping("/")
    public String testEndpoint(){
        return "Sucesso";
    }
}
