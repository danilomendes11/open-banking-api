package unicamp.mc946.openbankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stellar.sdk.KeyPair;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StellarService stellarService;

    public User createUser(String name, String login, String password){
        KeyPair keyPair = stellarService.createStellarUser();

        User user = new User(login, password, name, new String(keyPair.getSecretSeed()), keyPair.getAccountId());
        user = userRepository.save(user);

        return user;
    }
}
