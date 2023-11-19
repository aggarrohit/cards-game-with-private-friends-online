package club.friendsgame.cardrift.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Table {

    private int id;
    private String tableId;
    private List<Player> players;
    private List<Card> commonDeck;
    private List<Card> sideDeck;
    private Card activecard;
    private int activePlayerIndex;
    private ColorType activeColor;
    private boolean isCardDrawn;
    private boolean isUnoSaid;
    private boolean isReversed;
    private int numberOfPlayers;
}
