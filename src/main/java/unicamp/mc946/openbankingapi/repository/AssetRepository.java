package unicamp.mc946.openbankingapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unicamp.mc946.openbankingapi.model.Asset;

@Repository
public interface AssetRepository extends CrudRepository<Asset, String> {
}
