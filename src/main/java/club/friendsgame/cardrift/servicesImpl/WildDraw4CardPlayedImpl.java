package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.*;
import club.friendsgame.cardrift.services.WildDraw4CardPlayed;
import club.friendsgame.cardrift.utils.TableUtils;
import org.springframework.stereotype.Service;

@Service
public class WildDraw4CardPlayedImpl extends CardPlayedImpl implements WildDraw4CardPlayed {

    @Override
    public CardType getCardType() {
        return CardType.WILDDRAW4;
    }

    @Override
    public void playCard(Table table, Card card) {
        super.playCard(table, card);
        addCardsToNextPlayerDeck(table,4);
        play(card.getColorType(),1);
    }

    @Override
    public void addCardsToNextPlayerDeck(Table table, int numberOfCards) {
        Player playerNext = TableUtils.getPlayerWithIndex(table,TableUtils.getNextPreviousPlayerIndex(table));
        for (int i = 0; i < numberOfCards; i++) {
            playerNext.getDeck().add(TableUtils.takeRandomCard(table.getCommonDeck(),table));
        }
    }
}
