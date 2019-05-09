package pl.simplemethod.czujka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.simplemethod.czujka.model.RoomStatus;
import pl.simplemethod.czujka.model.Users;
import pl.simplemethod.czujka.repository.RoomStatusRepository;
import pl.simplemethod.czujka.repository.UsersRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class CzujkaService {
    @Autowired
    private UsersRepository repository;
    @Autowired
    private RoomStatusRepository roomRepository;

    private String text;

    public void saveTime(String user_name, String user_id) throws DateTimeException {
        //this line throws DateTimeException
        Time user_time = Time.valueOf(LocalTime.parse(text));

        Users find = repository.getUserByUsername(user_name);

        try {
            if (find != null) {
                repository.setUserTime(user_time, user_name);
            } else {
                repository.save(new Users(user_name, user_time, user_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean timeParse(String text) {
        this.text = text;

        this.text = this.text.replaceAll("([^0-9:])+", "");

        if ((this.text.length() > 4 && this.text.charAt(2) != ':') || this.text.length() < 4) {
            return false;

        } else if (this.text.length() == 4 && !this.text.contains(":")) {
            StringBuilder builder = new StringBuilder(this.text);
            builder.insert(2, ':');
            this.text = builder.toString();
        }
        return true;
    }

    public String getJsonMap() {
        List<RoomStatus> rooms = roomRepository.findAll();
        rooms.sort(Comparator.comparing(RoomStatus::getRoomNumber));

        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

        for (int i = 0; i < rooms.size(); i++) {
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("RoomNumber", rooms.get(i).getRoomNumber());
            object.put("Id", rooms.get(i).getId());
            object.put("Status", rooms.get(i).isOpen());

            array.add(object);
        }
        StringWriter jsonString = new StringWriter();

        try {
            array.writeJSONString(jsonString);
        } catch (IOException e) {
            System.err.println("Error! Can't create json map");
            e.printStackTrace();
        }

        return jsonString.toString();
    }

    public String getJsonList() {
        List<Users> users = repository.findAllByOrderByTimeAsc();
        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

        for (int i = 0; i < users.size(); i++) {
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("Time", users.get(i).getTime());
            object.put("Id", users.get(i).getId());
            object.put("UserName", users.get(i).getUsername());

            array.add(object);
        }
        StringWriter jsonString = new StringWriter();

        try {
            array.writeJSONString(jsonString);
        } catch (IOException e) {
            System.err.println("Error! Can't create json list");
            e.printStackTrace();
        }

        return jsonString.toString();
    }

    public String removeTimer(String user_name) throws Exception {
        String penultimateUser = getPenultimateUser();
        repository.delete(repository.getUserByUsername(user_name));

        return penultimateUser;
    }

    public String getPenultimateUser() {
        String user = "";
        try {
            user = repository.getPenultimateUserInQue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public Integer getQueue() {
        Integer queue = null;
        try {
            queue = repository.getYourQue(Time.valueOf(LocalTime.parse(text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queue;
    }
}
