package pl.simplemethod.czujka.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false, length = 100)
    private String username;
    @Column(nullable = false)
    private Time time;
    private String user_id;

    public Users() {}

    public Users(String username, Time time, String user_id) {
        this.username = username;
        this.time = time;
        this.user_id = user_id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User:" + username + "\t time :" + time;
    }
}
