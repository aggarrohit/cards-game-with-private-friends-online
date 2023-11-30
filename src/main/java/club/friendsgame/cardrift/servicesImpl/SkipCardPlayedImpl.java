package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.SkipCardPlayed;
import org.springframework.stereotype.Service;

@Service
public class SkipCardPlayedImpl extends CardPlayedImpl implements SkipCardPlayed {


    @Override
    public CardType getCardType() {
        return CardType.SKIP;
    }

    @Override
    public void playCard(Table table, Card card) {
        super.playCard(table, card);
        play(playedCard.getColorType(),1);
    }

    @Override
    public void updateCurrentPlayer2Times(Table table) {

    }
}
