package pl.simplemethod.czujka.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.NoSuchElementException;

@Service
public class CzujkaService {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private RoomStatusRepository roomRepository;

    private String text;
    private boolean reverse = false;

    public void saveTime(String user_name, String user_id) throws DateTimeException {
        //this line throws DateTimeException
        Time user_time = Time.valueOf(LocalTime.parse(text));

        String[] con = text.split(":");
        if(con[0].equals("01") || con[0].equals("02") || con.equals("03")) {
            System.out.println("jest mamy boolean wartosc " + text);
            this.reverse = true;
        }

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

    @SuppressWarnings("unchecked")
    public String getJsonMap() {
        List<RoomStatus> rooms = roomRepository.findAll();
        rooms.sort(Comparator.comparing(RoomStatus::getRoomNumber));

        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

        for (RoomStatus room : rooms) {
            JSONObject object = new JSONObject();
            if (room.isOpen()) {
                object.put("text", room.getRoomNumber() + " - *Otwarty*");
            } else {
                object.put("text", room.getRoomNumber() + " - Zamknięty");
            }

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

        String[] con = text.split(":");
        if(con[0].equals("01") || con[0].equals("02") || con.equals("03")) {
            System.out.println("jest mamy boolean wartosc " + text );
            this.reverse = false;
        }

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

        try {
            if(!reverse) {
                queue = repository.getYourQueBeforeMidnight(Time.valueOf(LocalTime.parse(text)));
            }
            else {
                queue = repository.getYourQueAfterMidnight(Time.valueOf(LocalTime.parse(text)));
            }

        } catch (Exception e) {}
        return queue;
    }

    public RoomStatus changeRoomStatus(Long id, Boolean status) {
        if (status) {
            roomRepository.openRoom(id);
        } else {
            roomRepository.closeRoom(id);
        }

        return roomRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
