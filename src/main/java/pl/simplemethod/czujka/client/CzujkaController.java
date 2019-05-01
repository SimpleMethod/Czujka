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
import pl.simplemethod.czujka.repository.UsersRepositoryImpl;


import javax.validation.ConstraintViolationException;
import java.sql.Date;

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("{\"message\":\"Incorrect parameters have been entered:\"}",headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        JSONObject request = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        request.put("message", "Incorrect parameters have been entered: "+e.getMessage());
        return new ResponseEntity<>(request.toString(),headers, HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>("",headers, HttpStatus.valueOf(201));
    }


    // TODO: 01.05.2019 Doddanie zapisu do bazy danych, zwrócenie którą osobą jest aktualnie zapisujący np. 2 oraz konwersja godziny do timestmap oraz zapisania jej w bazie 
    @PostMapping(path = "/czujka/zamykam", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerSave(HttpEntity<String> httpEntity, @RequestParam("token") String token, @RequestParam("user_name") String user_name, @RequestParam("text") String text) {
        if(!botController.tokenAuth(token))
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }
        logger.info(botController.postRichChatMessage(botController.getbotChannel()," ",stringParser.getSignUpGlobalBlock(user_name,text)));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        UsersRepository repository = new UsersRepositoryImpl();
        Users user = new Users(user_name, new Date(1111111111));
        repository.save(user);

        return new ResponseEntity<>(stringParser.getSignUpPerson(user_name,text), headers, HttpStatus.OK);
    }


    // TODO: 01.05.2019 Usunięcie użytkownika z bazy danych
    @PostMapping(path = "/czujka/wypisz", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerRemove(@RequestParam("token") String token, @RequestParam("user_name") String user_name) {

        if(!botController.tokenAuth(token))
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }
        logger.info(botController.postRichChatMessage(botController.getbotChannel()," ",stringParser.getUnsubscribeGlobalBlock(user_name)));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(stringParser.getUnsubscribePerson(), headers, HttpStatus.OK);
    }
}
