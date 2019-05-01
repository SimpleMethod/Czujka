package pl.simplemethod.czujka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.simplemethod.czujka.model.RoomStatus;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {
}
