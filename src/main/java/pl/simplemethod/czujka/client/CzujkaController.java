package pl.simplemethod.czujka.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.simplemethod.czujka.botparser.BotController;
import pl.simplemethod.czujka.botparser.StringParser;
import pl.simplemethod.czujka.model.RoomStatus;
import pl.simplemethod.czujka.model.Users;
import pl.simplemethod.czujka.repository.RoomStatusRepository;
import pl.simplemethod.czujka.repository.UsersRepository;


import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@Validated
@RequestMapping("v1.0")
public class CzujkaController {

    private static final Logger logger = LogManager.getLogger(CzujkaController.class);
    // TODO: 01.05.2019 Należy dodać reargowanie na sytuacje np. Brak chętnych do zamknięcia biura ale to jak będzie nam się chciało <raczej nie>

    @Autowired
    BotController botController;

    @Autowired
    StringParser stringParser;

    @Autowired
    private UsersRepository repository;
    @Autowired
    private RoomStatusRepository roomRepository;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("{\"message\":\"Incorrect parameters have been entered:\"}", headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        JSONObject request = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        request.put("message", "Incorrect parameters have been entered: " + e.getMessage());
        return new ResponseEntity<>(request.toString(), headers, HttpStatus.BAD_REQUEST);
    }


    // TODO: 01.05.2019 Wygenerowanie mapy  i zwrocenie jej
    @PostMapping(path = "/czujka/mapa", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerMaps(HttpEntity<String> httpEntity, @RequestParam("text") String token, @RequestParam("user_name") String user_name) {
        System.out.println(httpEntity.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<RoomStatus> rooms = roomRepository.findAllByRoomNumberOrderByRoomNumber();
        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

        for (int i = 0; i < rooms.size(); i++) {
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            System.out.println(rooms.get(i).getRoomNumber());
            object.put("RoomNumber", rooms.get(i).getRoomNumber());
            object.put("Id", rooms.get(i).getId());
            object.put("Status", rooms.get(i).isOpen());

            array.add(object);
        }

        StringWriter jsonString = new StringWriter();

        try {
            array.writeJSONString(jsonString);
        } catch (IOException e) {}

        return new ResponseEntity<>(jsonString.toString(), headers, HttpStatus.valueOf(201));
    }

    @PostMapping(path = "/czujka/lista", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerLists(HttpEntity<String> httpEntity, @RequestParam("text") String token, @RequestParam("user_name") String user_name) {
        System.out.println(httpEntity.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<Users> users = repository.findAllByOrderByTimeAsc();
        org.json.simple.JSONArray array = new org.json.simple.JSONArray();

       for (int i = 0; i < users.size(); i++) {
           org.json.simple.JSONObject object = new org.json.simple.JSONObject();
           System.out.println(users.get(i).getUsername());
           object.put("Time", users.get(i).getTime());
           object.put("Id", users.get(i).getId());
           object.put("UserName", users.get(i).getUsername());

           array.add(object);
       }

        StringWriter jsonString = new StringWriter();

        try {
            array.writeJSONString(jsonString);
        } catch (IOException e) {

        }
        return new ResponseEntity<>(jsonString.toString(), headers, HttpStatus.valueOf(201));
    }


    @PostMapping(path = "/czujka/zamykam", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerSave(HttpEntity<String> httpEntity, @RequestParam("token") String token, @RequestParam("user_name") String user_name, @RequestParam("text") String text) {
        if (!botController.tokenAuth(token)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }
        logger.info(botController.postRichChatMessage(botController.getbotChannel(), " ", stringParser.getSignUpGlobalBlock(user_name, text)));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        //usuwa wszystkie znaki poza cyframi i :
        text = text.replaceAll("([^0-9:])+", "");

        if ((text.length() > 4 && text.charAt(2) != ':') || text.length() < 4) {
            String channel = botController.getUserPrivateChannelID(user_name);
            botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonEnty());

        } else if (text.length() == 4 && !text.contains(":")) {
            //dodaje : do parsowania czasu ze stringa
            StringBuilder builder = new StringBuilder(text);
            builder.insert(2, ':');
            text = builder.toString();
        }

        Time user_time = Time.valueOf(LocalTime.parse(text));
        Users find = repository.getUserByUsername(user_name);
        Users user = new Users(user_name, user_time);

        try {
            if (find != null) {
                repository.setUserTime(user_time, user_name);
            } else {
                repository.save(user);
            }
        } catch (Exception e) {
            String channel = botController.getUserPrivateChannelID(user_name);
            botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonNull());
        }

        //zwraca ile osob jest przed toba do zamkniecia biura ; 0=zamykasz
        Integer que = repository.getYourQue(Time.valueOf(LocalTime.parse(text)));
        if (que == 0) {
            //pobieramy przedostatniego uzytkownika
            String penultimateUser = repository.getPenultimateUserInQue();

            if (penultimateUser != null) {
                String channel = botController.getUserPrivateChannelID(penultimateUser);
                botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonFound());
            }

            String channel = botController.getUserPrivateChannelID(penultimateUser);
            botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonAttend());

        } else {
            String channel = botController.getUserPrivateChannelID(user_name);
            botController.postRichChatMessage(channel, " ", stringParser.getQueuePerson(Integer.toString(que + 1)));
        }


        return new ResponseEntity<>(stringParser.getSignUpPerson(user_name, text), headers, HttpStatus.OK);
    }


    @PostMapping(path = "/czujka/wypisz", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerRemove(@RequestParam("token") String token, @RequestParam("user_name") String user_name) {

        if (!botController.tokenAuth(token)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }

        logger.info(botController.postRichChatMessage(botController.getbotChannel(), " ", stringParser.getUnsubscribeGlobalBlock(user_name)));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        String penultimateUser = "";

        try {
            penultimateUser = repository.getPenultimateUserInQue();

            repository.delete(repository.getUserByUsername(user_name));

        } catch (Exception exception) {
            return new ResponseEntity<>(stringParser.getLeavePersonNull(), headers, HttpStatus.OK);
        }

        String channel = botController.getUserPrivateChannelID(penultimateUser);
        botController.postRichChatMessage(channel, " ", stringParser.getLeavePenultimatePerson());

        return new ResponseEntity<>(stringParser.getLeavePerson(), headers, HttpStatus.OK);
    }
}
