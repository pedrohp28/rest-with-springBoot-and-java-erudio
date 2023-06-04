package restwithspringBootandjavaerudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restwithspringBootandjavaerudio.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
