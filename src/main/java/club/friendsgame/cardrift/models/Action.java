package club.friendsgame.cardrift.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Action {

    private String content;
    private String sender;
    private ActionType type;
}
