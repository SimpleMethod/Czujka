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

    List<RoomStatus> findAllByRoomNumberOrderByRoomNumber();

    @Modifying
    @Transactional
    @Query(value = "update room_status r set r.open = TRUE where r.room_number = :RoomNumber;", nativeQuery = true)
    void openRoom(@Param("RoomNumber")Integer roomNumber);

    @Modifying
    @Transactional
    @Query(value = "update room_status r set r.open = FALSE where r.room_number = :RoomNumber;", nativeQuery = true)
    void closeRoom(@Param("RoomNumber")Integer roomNumber);
}
