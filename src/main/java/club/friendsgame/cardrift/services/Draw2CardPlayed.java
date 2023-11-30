package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.Table;
import org.springframework.stereotype.Service;

@Service
public interface Draw2CardPlayed extends CardPlayed{
    void addCardsToNextPlayerDeck(Table table,int numberOfCards);
}
