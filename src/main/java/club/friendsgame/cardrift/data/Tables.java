package club.friendsgame.cardrift.data;

import club.friendsgame.cardrift.controllers.UserActionController;
import club.friendsgame.cardrift.models.*;
import club.friendsgame.cardrift.utils.TableUtils;

import java.util.*;


public class Tables {

    public static Map<String, Table> tables = new HashMap<>();

    public static void addTable(String tableId,int numberOfPlayers){
        List<Card> fullDeck = getFullDeck();
        Table table = Table.builder()
                .id(tables.size())
                .tableId(tableId)
                .commonDeck(fullDeck)
                .isCardDrawn(false)
                .isReversed(false)
                .unoSaidPhase(0)
                .sideDeck(new ArrayList<>())
                .players(new ArrayList<>())
                .numberOfPlayers(numberOfPlayers)
                .build();
        tables.put(tableId,table);

    }

    public static void addPlayerToTable(Player player,String tableId){
        Table table = tables.get(tableId);
        List<Player> players = table.getPlayers();
        player.setId(players.size());

        players.add(player);
    }

    public static boolean isAllPlayersJoined(String tableId){
        Table table = tables.get(tableId);
        return table.getPlayers().size() == table.getNumberOfPlayers();
    }

    public static void startGame(String tableId){
        Table table = tables.get(tableId);
        List<Player> players = table.getPlayers();
        // distribute 7 cards to players
        for(Player player:players){
            List<Card> cards = new ArrayList<>();

            for (int i = 0; i < 7; i++) {
                cards.add(TableUtils.takeRandomCard(table.getCommonDeck(),table));
            }
            player.setDeck(cards);
        }
        // set active card
        table.setActivecard(table.getCommonDeck().get(0));
        table.getCommonDeck().remove(0);
        // set active color
        table.setActiveColor(table.getActivecard().getColorType());
        // set active player index to 0
        table.setActivePlayerIndex(0);

        if(table.getActivecard().getCardType()!=CardType.NUMBER){
            proceedWithFirstSpecialCard(table);
        }


    }

    private static void proceedWithFirstSpecialCard(Table table){

        switch (table.getActivecard().getCardType()){

            case WILD:
                table.setActiveColor(null);
                break;
            case WILDDRAW4: {
                Player player = TableUtils.getActivePlayer(table);
                // add 4 cards to active player
                TableUtils.addCardsToPlayerDeck(table,4,player);
                // skip player
                TableUtils.changeActivePlayer(table,1);
                // set active color to null
                table.setActiveColor(null);
                break;
            }
            case SKIP:
                // skip player
                TableUtils.changeActivePlayer(table,1);
                break;
            case REVERSE:
                table.setReversed(true);
                // skip player
                TableUtils.changeActivePlayer(table,1);
                break;
            case DRAW2:
                Player player = TableUtils.getActivePlayer(table);
                // add 2 cards to active player
                TableUtils.addCardsToPlayerDeck(table,2,player);
                // skip player
                TableUtils.changeActivePlayer(table,1);
                break;
        }


    }

    public static boolean checkIfCardPlayedOk(Card playedCard,String tableId){

        Table table = tables.get(tableId);
        Card activeCard = table.getActivecard();

        // if accumulated cards present then only draw2 and wild-draw4 are valid
        if(table.getNumberOfAccumulatedCards()>0) return playedCard.getCardType()==CardType.DRAW2 || playedCard.getCardType()==CardType.WILDDRAW4;

        // wild-draw4 and wild cards are always valid if no accumulated cards present
        if(activeCard.getColorType()==null) return true;



        switch (playedCard.getCardType()){
            case NUMBER:{
                if(playedCard.getColorType()==activeCard.getColorType()) return true;
                if(playedCard.getNumber()==activeCard.getNumber()) return true;
                break;
            }
            case WILD:
            case WILDDRAW4: {
                return true;
            }
            case SKIP:
            case REVERSE:
            case DRAW2:
                // valid if card color is same
                if(playedCard.getColorType()==activeCard.getColorType()
                        // valid if card type is same
                        || playedCard.getCardType()==activeCard.getCardType()) return true;
                break;
        }

        return false;
    }

    public static void pickCard(String tableId, UserActionController userActionController){
        //get random card from common deck
        //remove that card from common deck
        Table  table = tables.get(tableId);
        Player activePlayer = TableUtils.getActivePlayer(table);
        if(table.isCardDrawn()){
            userActionController.sendToUser(activePlayer.getName(),"You have already picked card!");
        }else {
            // if there are accumulated cards then add those to current player
            if(table.getNumberOfAccumulatedCards()>0){
                TableUtils.addCardsToPlayerDeck(table,table.getNumberOfAccumulatedCards(),activePlayer);
                table.setNumberOfAccumulatedCards(0);
                TableUtils.updatePreviousPlayerIndex(table);
                // after picking accumulated cards the player is skipped
                TableUtils.changeActivePlayer(table,1);
                TableUtils.updateUnoSaidPhase(table);
                // for next player cardDrawn is false
                table.setCardDrawn(false);
            }else{
                TableUtils.addCardsToPlayerDeck(table,1,activePlayer);
                table.setCardDrawn(true);
            }

            userActionController.sendToTable(tableId);
        }
    }

    public static void skip(String tableId, UserActionController userActionController){

        Table  table = tables.get(tableId);
        Player activePlayer = TableUtils.getActivePlayer(table);
        if(table.isCardDrawn()) {
            TableUtils.updatePreviousPlayerIndex(table);
            TableUtils.changeActivePlayer(table,1);
            TableUtils.updateUnoSaidPhase(table);
            table.setCardDrawn(false);
            userActionController.sendToTable(tableId);
        }else {
            userActionController.sendToUser(activePlayer.getName(),"please draw a card first to skip");
        }
    }

    public static void unoSaid(String tableId, UserActionController userActionController){

        Table  table = tables.get(tableId);
        Player activePlayer = TableUtils.getActivePlayer(table);
        if(activePlayer.getDeck().size()!=2) {
            userActionController.sendToUser(activePlayer.getName(),"you are not eligible to say UNO");
        }else {

            boolean hasInvalidCards = activePlayer.getDeck().stream().filter(card -> Tables.checkIfCardPlayedOk(card,tableId)).toList().isEmpty();

            if (hasInvalidCards) {
                userActionController.sendToUser(activePlayer.getName(), "your cards are not eligible for UNO");
            } else {
                table.setUnoSaidPhase(2);
                userActionController.sendToTable(tableId);
                userActionController.sendToAllUsers(tableId, activePlayer.getName(), activePlayer.getName() + " said UNO!");
            }
        }
    }

    public static boolean unoSaid(String tableId){
        Table table = tables.get(tableId);
        Player player = TableUtils.getActivePlayer(table);
        if(player.getDeck().size()==2){
            table.setUnoSaidPhase(2);
            return true;
        }
        return false;
    }

    public static void caughtSaid(String tableId, UserActionController userActionController,String userName){
        Table table = tables.get(tableId);
        if(table.isChallengeActive()){
            userActionController.sendToUser(userName,"let the challenge decision complete!");
        }else {
            Player player = TableUtils.getPlayerWithIndex(table, table.getPreviousPlayerIndex());
            if (player.getDeck().size() == 1 && table.getUnoSaidPhase() != 1) {
                table.setUnoSaidPhase(0);
                TableUtils.addCardsToPlayerDeck(table, 2, player);
                userActionController.sendToTable(tableId);
            } else {
                userActionController.sendToUser(userName, "fake caught");
            }
        }
        // TODO: can add cards to player who said caught falsely

    }

    private static List<Card> getFullDeck(){
        List<Card> fullDeck = new ArrayList<>();

        ColorType[] colorTypes = {ColorType.GREEN,ColorType.YELLOW,ColorType.BLUE,ColorType.RED};

        for(ColorType colorType:colorTypes){
            addNumberCardsWithColor(fullDeck,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.SKIP,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.SKIP,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.DRAW2,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.DRAW2,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.WILDDRAW4,null);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.REVERSE,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.REVERSE,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.WILD,null);

        }

        Collections.shuffle(fullDeck);
        return fullDeck;
    }

    private static void addSpecialCardsWithTypeAndColor(List<Card> fullDeck,CardType cardType,ColorType colorType) {
        Card card = Card.builder()
                .cardType(cardType)
                .colorType(colorType)
                .build();

        fullDeck.add(card);
    }

    private static void addNumberCardsWithColor(List<Card> fullDeck, ColorType colorType) {
        Card card = Card.builder()
                .cardType(CardType.NUMBER)
                .colorType(colorType)
                .build();

        card.setNumber(0);
        fullDeck.add(card);

        add9cards(fullDeck,card);
        add9cards(fullDeck,card);
    }

    private static void add9cards(List<Card> fullDeck, Card card){
        for (int i = 1; i < 10; i++) {
            Card card1 = Card.builder()
                    .cardType(card.getCardType())
                    .colorType(card.getColorType())
                    .number(i)
                    .build();
            fullDeck.add(card1);
        }
    }

    public static void restartGame(String tableId, UserActionController userActionController, String userName) {

        userActionController.sendToAllUsers(tableId,userName,userName+" re-started game..");
        List<Card> fullDeck = getFullDeck();
        Table table = tables.get(tableId);
        table.setCommonDeck(fullDeck);
        table.setCardDrawn(false);
        table.setReversed(false);
        table.setUnoSaidPhase(0);
        table.setSideDeck(new ArrayList<>());
        for (Player player:table.getPlayers()) {
            player.setDeck(new ArrayList<>());
            player.setRank(0);
        }
        table.setPreviousPlayerIndex(0);
        table.setActivePlayerIndex(0);
        table.setGameCompleted(false);

        tables.put(tableId,table);
        startGame(tableId);
        userActionController.sendToTable(tableId);
    }

    public static void draw4Cards(String tableId, UserActionController userActionController) {
        Table table = Tables.tables.get(tableId);
        Player player = TableUtils.getActivePlayer(table);
        if(!table.isChallengeActive()){
            userActionController.sendToUser(player.getName(),"Invalid action!");
        }else {
            // add 4 cards to active player
            // add accumulated cards

            for (int i = 0; i < 4+table.getNumberOfAccumulatedCards(); i++) {
                player.getDeck().add(TableUtils.takeRandomCard(table.getCommonDeck(), table));
            }
            table.setNumberOfAccumulatedCards(0);
            // change previous/active player index
            table.setPreviousPlayerIndex(table.getActivePlayerIndex());
            TableUtils.changeActivePlayer(table, 1);
            // change challenge flag
            table.setChallengeActive(false);
            table.setBluff(false);
            userActionController.sendToTable(table.getTableId());
        }
    }

    public static void challengeDraw4(String tableId, UserActionController userActionController) {
        Table table = Tables.tables.get(tableId);
        Player activePlayer = TableUtils.getActivePlayer(table);
        if(!table.isChallengeActive()){
            userActionController.sendToUser(activePlayer.getName(),"Invalid action!");
        }else {
            if (table.isBluff()) {
                // previous player picks 4 cards
                // add accumulated cards
                Player playerPrevious = TableUtils.getPlayerWithIndex(table, table.getPreviousPlayerIndex());
                for (int i = 0; i < 4+ table.getNumberOfAccumulatedCards(); i++) {
                    playerPrevious.getDeck().add(TableUtils.takeRandomCard(table.getCommonDeck(), table));
                }
            } else {
                // add 6 cards to active player
                // add accumulated cards

                for (int i = 0; i < 6+ table.getNumberOfAccumulatedCards(); i++) {
                    activePlayer.getDeck().add(TableUtils.takeRandomCard(table.getCommonDeck(), table));
                }
                //change previous/active player
                table.setPreviousPlayerIndex(table.getActivePlayerIndex());
                TableUtils.changeActivePlayer(table, 1);
            }
            table.setNumberOfAccumulatedCards(0);
            // change challenge flag
            table.setChallengeActive(false);
            table.setBluff(false);
            userActionController.sendToTable(table.getTableId());
        }
    }
}
