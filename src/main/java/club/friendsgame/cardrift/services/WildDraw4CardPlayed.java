package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.Table;
import org.springframework.stereotype.Service;

@Service
public interface WildDraw4CardPlayed extends CardPlayed{
    void addCardsToNextPlayerDeck(Table table,int numberOfCards);
}
