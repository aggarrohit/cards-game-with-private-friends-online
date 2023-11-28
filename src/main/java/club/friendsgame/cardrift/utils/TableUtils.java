package club.friendsgame.cardrift.utils;

import club.friendsgame.cardrift.data.Tables;
import club.friendsgame.cardrift.models.Card;
import club.friendsgame.cardrift.models.Player;
import club.friendsgame.cardrift.models.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TableUtils {
    private TableUtils() {
    }

    public static Card takeRandomCard(List<Card> cards,Table table){

        // refill common deck is empty
        if(cards.isEmpty()){
            cards.addAll(table.getSideDeck());
            table.setSideDeck(new ArrayList<>());
        }

        int randomIndex = getRandomIntInRange(0,cards.size()-1);
        Card card = cards.get(randomIndex);
        cards.remove(randomIndex);
        return card;
    }

    private static int getRandomIntInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value must be less than or equal to max value.");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static void removeCardFromActivePlayer(Player player,Card playedCard,Table table){
        List<Card> playerCards = player.getDeck();
        Card cardToRemove = null;
        for (Card playerCard:playerCards){
            if(isCardsMatching(playerCard,playedCard)){
                cardToRemove = playerCard;
            }
        }
        if(cardToRemove!=null){
            playerCards.remove(cardToRemove);
        }
        player.setDeck(playerCards);
        List<Player> players = table.getPlayers();
        players.set(player.getId(),player);
//        table.setPlayers(players);//check
    }

    private static boolean isCardsMatching(Card baseCard,Card matcherCard){
        switch (baseCard.getCardType()){
            case NUMBER:{
                if( baseCard.getCardType()==matcherCard.getCardType() &&
                    baseCard.getColorType()==matcherCard.getColorType() &&
                    baseCard.getNumber()==matcherCard.getNumber()){
                    return true;
                }
                break;
            }
            case WILD:
            case WILDDRAW4: if( baseCard.getCardType()==matcherCard.getCardType() && matcherCard.getColorType()!=null){
                return true;
            }
                break;
            case SKIP:
            case REVERSE:
            case DRAW2:
                // valid if card color is same
                if( baseCard.getCardType()==matcherCard.getCardType() &&
                        baseCard.getColorType()==matcherCard.getColorType()){
                    return true;
                }
                break;
        }
        return false;
    }

    public static void updatePreviousPlayerIndex(Table table) {
        table.setPreviousPlayerIndex(table.getActivePlayerIndex());
    }

    public static void changeActivePlayer(Table table,int times) {
        for (int i = 0; i < times; i++) {
            table.setActivePlayerIndex(getNextPreviousPlayerIndex(table));
        }
    }

    public static int getNextPreviousPlayerIndex(Table table){
        int activePlayerIndex = table.getActivePlayerIndex();
        if(table.isReversed()){
            return getPreviousValidPlayerIndex(activePlayerIndex, table);
        }else{
            return  getNextValidPlayerIndex(activePlayerIndex, table);
        }
    }

    private static int getNextValidPlayerIndex(int currentIndex,Table table){

        int nextIndex = currentIndex+1;
        if(nextIndex == table.getPlayers().size()) nextIndex = 0;

        boolean foundCorrectIndex = false;
        do {
            if(getPlayerWithIndex(table,nextIndex).getRank()==0) {
                foundCorrectIndex = true;
            }else{
                if(nextIndex+1 == table.getPlayers().size()) {
                    nextIndex = 0;
                }else{
                    nextIndex++;
                }
            }
        }while (!foundCorrectIndex);

        return nextIndex;

    }

    private static int getPreviousValidPlayerIndex(int currentIndex,Table table){

        int previousIndex = currentIndex-1;
        if(currentIndex == 0) previousIndex = table.getPlayers().size()-1;

        boolean foundCorrectIndex = false;
        do {
            if(getPlayerWithIndex(table,previousIndex).getRank()==0) {
                foundCorrectIndex = true;
            }else{
                if(previousIndex == 0) {
                    previousIndex = table.getPlayers().size()-1;
                }else{
                    previousIndex--;
                }
            }
        }while (!foundCorrectIndex);

        return previousIndex;

    }

    public static Player getPlayerFromUserName(String userName, Table table){
        return table.getPlayers().stream().filter(player1 -> !Objects.equals(player1.getName(), userName)).toList().get(0);
    }

    public static void addCardsToPlayerDeck(Table table, int numberOfCards,Player player){
        for (int i = 0; i < numberOfCards; i++) {
            player.getDeck().add(TableUtils.takeRandomCard(table.getCommonDeck(),table));
        }
    }

    public static boolean checkIfActivePlayer(String tableId, String userName) {
        Table table = Tables.tables.get(tableId);
        Player activePlayer = getActivePlayer(table);
        return Objects.equals(activePlayer.getName(), userName);
    }

    public static void updateUnoSaidPhase(Table table) {
        if(table.getUnoSaidPhase()>0){
            table.setUnoSaidPhase(table.getUnoSaidPhase()-1);
        }
    }

    public static Player getActivePlayer(Table table){
       return table.getPlayers().stream().filter(player -> player.getId()== table.getActivePlayerIndex()).toList().get(0);
    }

    public static Player getPlayerWithIndex(Table table,int index){
        return table.getPlayers().stream().filter(player -> player.getId() == index).toList().get(0);
    }
}
