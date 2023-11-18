package club.friendsgame.cardrift.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessaage {

    private String content;
    private String sender;
    private MessageType type;
}
