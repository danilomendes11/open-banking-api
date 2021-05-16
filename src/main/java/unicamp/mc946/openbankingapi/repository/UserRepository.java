package unicamp.mc946.openbankingapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unicamp.mc946.openbankingapi.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {


}
