package club.friendsgame.cardrift.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TableCommons {

    private int id;
    private String tableId;
    private List<PlayerCommons> playersCommons;
    private Card activecard;
    private int activePlayerIndex;
    private ColorType activeColor;
    private ColorType previousColor;
    private boolean isCardDrawn;
    private int unoSaidPhases;
    private boolean isReversed;
    private boolean isChallengeActive;
    private int numberOfPlayers;
    private boolean isGameCompleted;
}
