package club.friendsgame.cardrift.models;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ToString
public class Card {

    private CardType cardType;
    private ColorType colorType;
    private int number;
}
