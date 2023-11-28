package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.NumberCardPlayed;
import org.springframework.stereotype.Service;

@Service
public class NumberCardPlayedImpl extends CardPlayedImpl implements NumberCardPlayed {


    @Override
    public CardType getCardType() {
        return CardType.NUMBER;
    }

    @Override
    public void playCard(Table table, Card card){
        super.playCard(table, card);
        play(card.getColorType(),0);
    }


}
