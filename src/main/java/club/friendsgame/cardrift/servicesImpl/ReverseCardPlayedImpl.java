package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.CardType;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.ReverseCardPlayed;
import club.friendsgame.cardrift.utils.TableUtils;
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
        int numberOfActivePlayers = table.getPlayers().stream().filter(player -> player.getRank()==0).toList().size();
        // if there are only 2 active players then reverse works as skip
        if(numberOfActivePlayers==2){
            play(playedCard.getColorType(),1);
        }else{
            play(playedCard.getColorType(),0);
        }

    }



    @Override
    public void updateIsReversed(Table table) {
        table.setReversed(!table.isReversed());
    }
}
