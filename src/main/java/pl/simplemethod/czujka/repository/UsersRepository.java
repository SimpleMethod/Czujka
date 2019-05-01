package pl.simplemethod.czujka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.simplemethod.czujka.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
