package club.friendsgame.cardrift.controllers;

import club.friendsgame.cardrift.data.Tables;
import club.friendsgame.cardrift.mappers.TableMapper;
import club.friendsgame.cardrift.models.Action;
import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.Player;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.CardPlayed;
import club.friendsgame.cardrift.services.MyCardManager;
import club.friendsgame.cardrift.utils.TableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class UserActionController {

    private final SimpMessageSendingOperations messageSendingOperations;
    @Autowired
    MyCardManager myCardManager;

    @MessageMapping("/createTable")
    @SendTo("/table")
    public String createTable(@Payload Action action,SimpMessageHeaderAccessor accessor) throws AuthenticationException {

        int numberOfPlayers = Integer.parseInt(action.getContent());
        String tableId = generateRandomTableId();
        Tables.addTable(tableId,numberOfPlayers);
        addPlayerToTable(tableId, accessor);

        sendTableIdToUser(getUsername(accessor),tableId);
        return tableId;
    }


    public void proceedToPlayCard(Card card, String tableId){
        Table table = Tables.tables.get(tableId);
        CardPlayed cardPlayed = myCardManager.getServicesByType(card.getCardType()).get(0);
        cardPlayed.playCard(table,card);
    }


    @MessageMapping("/subscribeToTable/{tableId}")
    @SendTo("/table/{tableId}")
    public String subscribeToTable(@Payload Action action, SimpMessageHeaderAccessor accessor) throws AuthenticationException, InterruptedException {
        String tableId = action.getContent();
        addPlayerToTable(tableId,accessor);
        if(Tables.isAllPlayersJoined(tableId)){
            Tables.startGame(tableId);
            Thread.sleep(250);
            sendToTable(tableId);
        }
        return "message received in table by user : "+getUsername(accessor)+" content : "+action.getContent();
    }

    @MessageMapping("/cardPlayed/{tableId}")
    public void cardPlayed(@Payload Card card, SimpMessageHeaderAccessor accessor) throws AuthenticationException {
        String tableId = accessor.getDestination().substring("/cardPlayed/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);

        boolean isCardPlayedValid = Tables.checkIfCardPlayedOk(card,tableId);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else
        if(!isCardPlayedValid){
            sendToUser(getUsername(accessor),"Card not valid!");
        }else{
            proceedToPlayCard(card,tableId);
        }
 }

    @MessageMapping("/pickCard/{tableId}")
    public void pickCard(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/pickCard/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else
        {
            Tables.pickCard(tableId,this);
        }
    }

    @MessageMapping("/skip/{tableId}")
    public void skip(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/skip/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else{
            Tables.skip(tableId,this);
        }
    }

    @MessageMapping("/unoSaid/{tableId}")
    public void unoSaid(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/unoSaid/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else{
            Tables.unoSaid(tableId,this);
        }
    }

    @MessageMapping("/caughtSaid/{tableId}")
    public void caughtSaid(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/caughtSaid/".length()+4);

        String userName = getUsername(accessor);

        Tables.caughtSaid(tableId,this,userName);

    }

    @MessageMapping("/restartGame/{tableId}")
    public void restartGame(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/restartGame/".length()+4);

        String userName = getUsername(accessor);
        Table table = Tables.tables.get(tableId);

        if(!table.isGameCompleted()){
            sendToUser(userName,"Game is not completed yet!");
        }else{
            Tables.restartGame(tableId,this,userName);
        }

    }

    @MessageMapping("/draw4Cards/{tableId}")
    public void draw4Cards(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/draw4Cards/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else
        {
            Tables.draw4Cards(tableId,this);
        }
    }


    @MessageMapping("/challengeDraw4/{tableId}")
    public void challengeDraw4(SimpMessageHeaderAccessor accessor) throws AuthenticationException, JsonProcessingException {
        String tableId = accessor.getDestination().substring("/challengeDraw4/".length()+4);

        String userName = getUsername(accessor);

        boolean isActivePlayer = TableUtils.checkIfActivePlayer(tableId,userName);


        if(!isActivePlayer){
            sendToUser(getUsername(accessor),"its not your turn!");
        }else
        {
            Tables.challengeDraw4(tableId,this);
        }
    }


    private String getUsername(SimpMessageHeaderAccessor accessor) throws AuthenticationException {
        try {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            return (String) sessionAttributes.get("username");
        }catch (NullPointerException e){
            throw new AuthenticationException("user not connected");
        }

    }

    private String generateRandomTableId() {
        // Generate a random 4-digit number
        Random random = new Random();
        int randomInt = 1000 + random.nextInt(9000);

        return String.valueOf(randomInt);
    }

    public void sendToUser(String username,String message){
        messageSendingOperations.convertAndSend("/user/"+username+"/queue/private-message",message);
    }

    public void sendToAllUsers(String tableId,String username,String message){
        Table table = Tables.tables.get(tableId);
        for (Player player:table.getPlayers()){
            messageSendingOperations.convertAndSend("/user/"+player.getName()+"/queue/private-message",message);
        }
    }

    public void sendTableIdToUser(String username,String message){
        messageSendingOperations.convertAndSend("/user/"+username+"/callback/tableid",message);
    }

    public void sendToTable(String tableId){

        messageSendingOperations.convertAndSend("/table/"+tableId,"all players joined ..");
        Table table = Tables.tables.get(tableId);
        messageSendingOperations.convertAndSend("/table/"+tableId+"/table-updates", TableMapper.toTableCommons(table));

        for (Player player:table.getPlayers()){
            messageSendingOperations.convertAndSend("/user/"+player.getName()+"/queue/my-cards",player.getDeck());
        }

    }

    private void addPlayerToTable(String tableId, SimpMessageHeaderAccessor accessor) throws AuthenticationException {
        Table table = Tables.tables.get(tableId);

        Player player = Player.builder()
                .id(table.getPlayers().size())
                .name(getUsername(accessor))
                .build();
        Tables.addPlayerToTable(player, tableId);
        messageSendingOperations.convertAndSend("/table/"+ tableId,getUsername(accessor)+" joined the table");
    }
}
