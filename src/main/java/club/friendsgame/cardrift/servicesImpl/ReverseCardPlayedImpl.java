package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.ReverseCardPlayed;
import org.springframework.stereotype.Service;

@Service
public class ReverseCardPlayedImpl extends CardPlayedImpl implements ReverseCardPlayed {


    @Override
    public CardType getCardType() {
        return CardType.REVERSE;
    }

    @Override
    public void playCard(Table table, Card card) {
        super.playCard(table, card);
        updateIsReversed(table);
        play(playedCard.getColorType(),0);
    }



    @Override
    public void updateIsReversed(Table table) {
        table.setReversed(!table.isReversed());
    }
}
