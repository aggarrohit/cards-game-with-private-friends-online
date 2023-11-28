package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.WildCardPlayed;
import org.springframework.stereotype.Service;

@Service
public class WildCardPlayedImpl extends CardPlayedImpl implements WildCardPlayed {


    @Override
    public CardType getCardType() {
        return CardType.WILD;
    }

    @Override
    public void playCard(Table table, Card card) {
        super.playCard(table, card);
        play(card.getColorType(),0);
    }

}
