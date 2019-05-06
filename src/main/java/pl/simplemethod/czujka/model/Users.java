package pl.simplemethod.czujka.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private Time time;

    public Users() {}

    public Users(String username, Time time) {
        this.username = username;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User:" + username + "\t time :" + time;
    }
}
