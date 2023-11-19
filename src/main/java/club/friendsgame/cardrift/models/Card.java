package club.friendsgame.cardrift.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    private CardType cardType;
    private ColorType colorType;
    private int number;
}
