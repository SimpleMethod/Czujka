package pl.simplemethod.czujka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room_status")
public class RoomStatus {
    @Id
    @GeneratedValue
    private Long id;

    private String roomNumber;
    private boolean open = false;

    public RoomStatus() {}

    public RoomStatus(String roomNumber, boolean open) {
        this.roomNumber = roomNumber;
        this.open = open;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
