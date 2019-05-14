package pl.simplemethod.czujka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.simplemethod.czujka.model.RoomStatus;
import pl.simplemethod.czujka.model.Users;
import pl.simplemethod.czujka.repository.RoomStatusRepository;
import pl.simplemethod.czujka.repository.UsersRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.*;

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

    @SuppressWarnings("unchecked")
    public String getJsonList() {
        List<Users> users = repository.findAllByOrderByTimeDesc();
        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

        for (int i = 0; i < users.size(); i++) {
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("text", String.valueOf(i + 1) +
                    " - " +
                    users.get(i).getTime() +
                    " - " +
                    users.get(i).getUsername());
            array.add(object);
        }

        if (array.isEmpty()) {
            return null;
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
        }
        return user;
    }

    public Integer getQueue() {
        Integer queue = null;

        Date date = new Date();

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date midnight = c.getTime();

        try {
            if(midnight.before(date)) {
                queue = repository.getYourQueBeforeMidnight(Time.valueOf(LocalTime.parse(text)));
            }
            else {
                queue = repository.getYourQueAfterMidnight(Time.valueOf(LocalTime.parse(text)));
            }

        } catch (Exception e) {}
        return queue;
    }

}
