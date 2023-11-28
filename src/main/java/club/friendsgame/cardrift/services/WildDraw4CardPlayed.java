package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.Table;
import org.springframework.stereotype.Service;

@Service
public interface WildDraw4CardPlayed extends CardPlayed{
//    ColorType getColorFromPlayer(Table table);
    void addCardsToNextPlayerDeck(Table table,int numberOfCards);
}
