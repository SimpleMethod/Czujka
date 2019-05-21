package pl.simplemethod.czujka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.simplemethod.czujka.model.RoomStatus;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Component
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {

    List<RoomStatus> findAll();

    @Modifying
    @Transactional
    @Query(value = "UPDATE room_status r SET r.open = TRUE where r.id = :id", nativeQuery = true)
    void openRoom(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE room_status r SET r.open = FALSE WHERE r.id = :id", nativeQuery = true)
    void closeRoom(@Param("id") Long id);
}
