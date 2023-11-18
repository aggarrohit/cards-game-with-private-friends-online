package club.friendsgame.cardrift.configurations;

import club.friendsgame.cardrift.models.ChatMessaage;
import club.friendsgame.cardrift.models.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info("User disconnected: {}",username);
            var chatMessage = ChatMessaage.builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
                    .build();
            messageSendingOperations.convertAndSend("/table/public",chatMessage);
        }
    }

}
