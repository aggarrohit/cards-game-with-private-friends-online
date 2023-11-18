package club.friendsgame.cardrift.controllers;

import club.friendsgame.cardrift.models.Action;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
public class UserActionController {


    @MessageMapping("/action")
    @SendTo("/table/public")
    public String greeting(@Payload Action action, SimpMessageHeaderAccessor accessor) throws AuthenticationException {

        return "greetings from username : "+getUsername(accessor)+" with action type as : "+action.getType().toString();
    }

    private String getUsername(SimpMessageHeaderAccessor accessor) throws AuthenticationException {
        try {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            return (String) sessionAttributes.get("username");
        }catch (NullPointerException e){
            throw new AuthenticationException("user not connected");
        }

    }

}
