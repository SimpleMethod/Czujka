package pl.simplemethod.czujka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.simplemethod.czujka.model.Users;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.List;


@Repository
@Component
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users getUserByUsername(String username);

    List<Users> findAllByOrderByTimeDesc();

    @Modifying
    @Transactional
    @Query(value = "update users u set u.time = :NewTime where u.username = :Username", nativeQuery = true)
    void setUserTime(@Param("NewTime") Time time, @Param("Username")String username);

    @Query(value = "SELECT COUNT(*) FROM users WHERE users.time > :UserTime", nativeQuery = true)
    Integer getYourQueBeforeMidnight(@Param("UserTime")Time time);

    @Query(value = "SELECT COUNT(*) FROM users WHERE users.time < :UserTime and users.time < '03:00'", nativeQuery = true)
    Integer getYourQueAfterMidnight(@Param("UserTime")Time time);

    @Query(value = "SELECT user_id FROM users ORDER BY users.time DESC LIMIT 1 offset 1", nativeQuery = true)
    String getPenultimateUserInQue();
}
