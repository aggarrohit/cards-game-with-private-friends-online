package club.friendsgame.cardrift.controllers;

import club.friendsgame.cardrift.models.Action;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.Random;

@RestController
public class UserActionController {

    @GetMapping("/generateTable")
    public String generateTable(){
        String tableId = generateRandomTopicName();
        createTable(tableId);
        return tableId;
    }

    @MessageMapping("/createTable")
    @SendTo("/table")
    public String createTable(String tableId){
        return tableId;
    }

    @MessageMapping("/action")
    @SendTo("/table/public")
    public String greeting(@Payload Action action, SimpMessageHeaderAccessor accessor) throws AuthenticationException {

        return "greetings from username : "+getUsername(accessor)+" with action type as : "+action.getType().toString();
    }

    @MessageMapping("/subscribeToTable/{tableId}")
    @SendTo("/table/{tableId}")
    public String subscribeToTable(@Payload Action action, SimpMessageHeaderAccessor accessor) throws AuthenticationException {

        return "message received in table by user : "+getUsername(accessor)+" content : "+action.getContent();
    }

    private String getUsername(SimpMessageHeaderAccessor accessor) throws AuthenticationException {
        try {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            return (String) sessionAttributes.get("username");
        }catch (NullPointerException e){
            throw new AuthenticationException("user not connected");
        }

    }

    private String generateRandomTopicName() {
        // Generate a random 4-digit number
        Random random = new Random();
        int randomInt = 1000 + random.nextInt(9000);

        return String.valueOf(randomInt);
    }

}
