package club.friendsgame.cardrift.models;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ToString
public class Table {

    private int id;
    private String tableId;
    private List<Player> players;
    private List<Card> commonDeck;
    private List<Card> sideDeck;
    private Card activecard;
    private int activePlayerIndex;
    private ColorType activeColor;
    private ColorType previousColor;
    private boolean isCardDrawn;
    private boolean isBluff;
    private int unoSaidPhase;//2 current player said uno, 1 player who said uno has played or skipped, 0 uno said dismissed
    private boolean isReversed;
    private boolean isChallengeActive;
    private int numberOfPlayers;
    private int previousPlayerIndex;
    private boolean isGameCompleted;
    private int numberOfAccumulatedCards;
}
