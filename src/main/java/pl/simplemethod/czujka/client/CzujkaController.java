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
import pl.simplemethod.czujka.service.CzujkaService;

import javax.validation.ConstraintViolationException;
import java.time.DateTimeException;

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
    private CzujkaService service;

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


    @PostMapping(path = "/czujka/mapa", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerMaps(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();
        if (!botController.tokenAuth(token)) {
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(stringParser.getRoomList(service.getJsonMap()), headers, HttpStatus.valueOf(201));
    }

    @PostMapping(path = "/czujka/lista", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerLists(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (!botController.tokenAuth(token)) {
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(stringParser.getPersonList(service.getJsonList()), headers, HttpStatus.valueOf(201));
    }

    @PostMapping(path = "/czujka/zamykam", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerSave(@RequestParam("token") String token, @RequestParam("user_name") String user_name, @RequestParam("text") String text, @RequestParam("user_id")String user_id) {
        if (!botController.tokenAuth(token)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        String channel = botController.getUserPrivateChannelID(user_id);

        if(!service.timeParse(text)) {
            botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonEntry());
        }

        try {
            service.saveTime(user_name, user_id);
        } catch (DateTimeException e) {
            return new ResponseEntity<>(stringParser.getLeavePersonEntryPerson(), headers, HttpStatus.OK);
        }
        logger.info(botController.postRichChatMessage(botController.getbotChannel(), " ", stringParser.getSignUpGlobalBlock(user_name, text)));
        Integer que = service.getQueue();
        if (que == 0) {
            String penultimateUser = service.getPenultimateUser();

            if (penultimateUser != null) {
                channel = botController.getUserPrivateChannelID(penultimateUser);
                botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonFound());
            } else {
                botController.postRichChatMessage(channel, " ", stringParser.getLeavePersonAttend());
            }

        } else {
            botController.postRichChatMessage(channel, " ", stringParser.getQueuePerson(Integer.toString(que + 1)));
        }

        return new ResponseEntity<>(stringParser.getSignUpPerson(user_name, text), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/czujka/wypisz", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> controllerRemove(@RequestParam("token") String token, @RequestParam("user_name") String user_name) {

        HttpHeaders headers = new HttpHeaders();
        String penultimateUser = "";
        headers.add("Content-Type", "application/json");
        if (!botController.tokenAuth(token)) {
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("", headers, HttpStatus.UNAUTHORIZED);
        }
        try {
            penultimateUser = service.removeTimer(user_name);
        } catch (Exception exception) {
            return new ResponseEntity<>(stringParser.getLeavePersonNull(), headers, HttpStatus.OK);
        }
        logger.info(botController.postRichChatMessage(botController.getbotChannel(), " ", stringParser.getUnsubscribeGlobalBlock(user_name)));
        String channel = botController.getUserPrivateChannelID(penultimateUser);
        botController.postRichChatMessage(channel, " ", stringParser.getLeavePenultimatePerson());

        return new ResponseEntity<>(stringParser.getUnsubscribePerson(), headers, HttpStatus.OK);
    }
}
