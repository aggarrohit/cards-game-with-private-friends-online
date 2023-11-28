package club.friendsgame.cardrift.servicesImpl;

import club.friendsgame.cardrift.controllers.UserActionController;
import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.ColorType;
import club.friendsgame.cardrift.models.Player;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.services.CardPlayed;
import club.friendsgame.cardrift.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public abstract class CardPlayedImpl implements CardPlayed {

    Table table;
    Card playedCard;
//    Player player;

    @Autowired
    UserActionController userActionController;

    protected CardPlayedImpl() {

    }

    @Override
    public void playCard(Table table, Card card){
        this.table = table;
        this.playedCard = card;
    }

    public void play(ColorType colorType,int skipPlayer){

        Player player = TableUtils.getActivePlayer(table);
        updateSideDeck();
        updateActiveCard();
        changeActiveCardColor(colorType);
        updatePreviousPlayerIndex();
        removeCardFromActivePlayer(player);
        changeActivePlayer(skipPlayer+1);
        updateUnoSaidPhase();
        checkIfPlayerWon(player);
        table.setGameCompleted(checkIfGameCompleted());
        table.setCardDrawn(false);
        userActionController.sendToTable(table.getTableId());
    }

    @Override
    public void updateSideDeck() {
        List<Card> sideDeck = table.getSideDeck();
        sideDeck.add(table.getActivecard());
    }

    @Override
    public void updateActiveCard() {
        table.setActivecard(playedCard);
    }

    @Override
    public void changeActiveCardColor(ColorType colorType) {
        table.setActiveColor(colorType);
    }

    @Override
    public void updatePreviousPlayerIndex() {
        TableUtils.updatePreviousPlayerIndex(table);
    }

    @Override
    public void removeCardFromActivePlayer(Player player) {
        TableUtils.removeCardFromActivePlayer(player,playedCard,table);
    }

    @Override
    public void changeActivePlayer(int times) {
        TableUtils.changeActivePlayer(table,times);
    }

    @Override
    public void updateUnoSaidPhase() {
        TableUtils.updateUnoSaidPhase(table);
    }

    @Override
    public boolean checkIfPlayerWon(Player player) {
        if(player.getDeck().isEmpty()){
            player.setRank(getAvailableRank(table));
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfGameCompleted() {
        return table.getPlayers().stream().filter(player -> player.getRank() == 0).toList().size() == 1;
    }



    private static int getAvailableRank(Table table){
        Optional<Player> playerWithMaxRank = table.getPlayers().stream().max(Comparator.comparingInt(Player::getRank));
        return playerWithMaxRank.map(player -> player.getRank() + 1).orElse(0);
    }
}
