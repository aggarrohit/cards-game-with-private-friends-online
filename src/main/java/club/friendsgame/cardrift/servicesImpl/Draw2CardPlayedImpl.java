package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Player;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.Draw2CardPlayed;
import club.friendsgame.cardrift.utils.TableUtils;
import org.springframework.stereotype.Service;

@Service
public class Draw2CardPlayedImpl extends CardPlayedImpl implements Draw2CardPlayed {

    @Override
    public CardType getCardType() {
        return CardType.DRAW2;
    }

    @Override
    public void addCardsToNextPlayerDeck(Table table, int numberOfCards) {
        Player playerNext = TableUtils.getPlayerWithIndex(table,TableUtils.getNextPreviousPlayerIndex(table));
        TableUtils.addCardsToPlayerDeck(table,2,playerNext);
    }

    @Override
    public void playCard(Table table, Card card) {
        super.playCard(table, card);
        addCardsToNextPlayerDeck(table,2);
        play(playedCard.getColorType(),1);
    }
}
