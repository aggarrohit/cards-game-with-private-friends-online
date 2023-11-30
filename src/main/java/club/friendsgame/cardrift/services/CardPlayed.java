package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.*;
import org.springframework.stereotype.Service;

@Service
public interface CardPlayed {

    CardType getCardType();
    void updateSideDeck();

    void updateActiveCard();

    void changeActiveCardColor(ColorType colorType);

    void updatePreviousPlayerIndex();

    void removeCardFromActivePlayer(Player player);

    void changeActivePlayer(int times);

    void updateUnoSaidPhase();

    void playCard(Table table, Card card);

    boolean checkIfPlayerWon(Player player);

    boolean checkIfGameCompleted();
}
