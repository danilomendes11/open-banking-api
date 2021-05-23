package unicamp.mc946.openbankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stellar.sdk.Asset;
import unicamp.mc946.openbankingapi.model.User;
import unicamp.mc946.openbankingapi.repository.AssetRepository;
import unicamp.mc946.openbankingapi.repository.UserRepository;

@Service
public class AssetService {

    @Autowired
    private StellarService stellarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    public void issueAssetForAccount(String name, String login, Double value) {
        User user = userRepository.findByLogin(login);
        Asset asset = stellarService.issueAsset(name, user.getPrivateKey(), value);
        System.out.println(asset.toString());
        assetRepository.save(new unicamp.mc946.openbankingapi.model.Asset(name, user.getStellarAccId()));
    }

}
