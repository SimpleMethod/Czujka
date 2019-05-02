package pl.simplemethod.czujka.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.simplemethod.czujka.botparser.BotController;
import pl.simplemethod.czujka.botparser.StringParser;
import pl.simplemethod.czujka.model.Users;
import pl.simplemethod.czujka.repository.UsersRepository;


import javax.validation.ConstraintViolationException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        return new ResponseEntity<>("", headers, HttpStatus.valueOf(201));
    }


    // TODO: 01.05.2019 Zwrócenie listy osób zamykający  
    @PostMapping(path = "/czujka/lista", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerLists(HttpEntity<String> httpEntity, @RequestParam("text") String token, @RequestParam("user_name") String user_name) {
        System.out.println(httpEntity.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<Users> users = repository.findAllByOrderByTimeAsc();

        //todo zwracac liste json
        users.forEach(System.out::println);

        //uwaga ustawione na komunikat o wypisaniu
        return new ResponseEntity<>(stringParser.getUnsubscribeGlobalBlock(user_name), headers, HttpStatus.valueOf(201));
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

        if((text.length() > 4 && text.charAt(2) != ':') || text.length() < 4) {
            //todo Zakończ z  komunikatem o błędnie wpisanym czasie, za dużo lub za malo cyfr lub źle wpisany dwukropek
        } else if(text.length() == 4 && !text.contains(":")) {
            //dodaje : do parsowania czasu ze stringa
            StringBuilder builder = new StringBuilder(text);
            builder.insert(2, ':');
            text = builder.toString();

        }

        Time user_time = Time.valueOf(LocalTime.parse(text));
        Users find = repository.getUserByUsername(user_name);
        Users user = new Users(user_name, user_time);

        //sprawdzamy czy uzytkownik jest juz w bazie
        if (find != null) {
            //todo zwroc komunikat o zmianie czasu moze byc zrealizowane z komunikatem ponizej
            repository.setUserTime(user_time, user_name);

            //zwraca ile osob jest przed toba do zamkniecia biura ; 0=zamykasz
            int que = repository.getYourQue(Time.valueOf(LocalTime.parse(text)));
            if(que == 0) {
                //pobieramy przedostatniego uzytkownika
                String penultimateUser = repository.getPenultimateUserInQue();

                if(penultimateUser != null) {
                    //todo WYSLIJ WIADOMOSC DO UZYTKOWNIKA ZE NIE JEST OSTATNI
                    System.out.println(penultimateUser + "Juz nie jestes ostatni !");
                }

                //todo WYSLIJ UZYTKOWNIKOWI INFO ZE OSTATNI
                System.out.println("Jestes ostatni w kolejce! ZAMYKASZ :)");
            } else {
                //todo ZWROC KOMUNIKAT O NUMERZE W KOLEJCE
                System.out.println("Jesteś " + que + 1 + " do zamkniecia biura");
            }

        } else {
            //todo trycatch ?
            repository.save(user);
        }

        return new ResponseEntity<>(stringParser.getSignUpPerson(user_name, text), headers, HttpStatus.OK);
    }


    // TODO: 01.05.2019 Usunięcie użytkownika z bazy danych
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

        String penultimateUser = repository.getPenultimateUserInQue();
        //todo zwroc komunikat ze sie wypisal
        repository.delete(repository.getUserByUsername(user_name));
        //todo napisz komunikat do penultimateUser ze jest ostatni

        return new ResponseEntity<>(stringParser.getUnsubscribePerson(), headers, HttpStatus.OK);
    }
}
