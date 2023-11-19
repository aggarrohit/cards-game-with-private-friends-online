package club.friendsgame.cardrift.data;

import club.friendsgame.cardrift.models.*;

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
                .isUnoSaid(false)
                .sideDeck(new ArrayList<>())
                .players(new ArrayList<>())
                .numberOfPlayers(numberOfPlayers)
                .build();
        tables.put(tableId,table);
    }

    public static void addPlayerToTable(Player player,String tableId){
        Table table = tables.get(tableId);
        List<Player> players = table.getPlayers();
        players.add(player);
        table.setPlayers(players);
        tables.put(tableId,table);
        if(players.size()==table.getNumberOfPlayers()) startGame(table);
    }

    public static void startGame(Table table){
        // shuffle players
        List<Player> players = table.getPlayers();
        Collections.shuffle(players);
        table.setPlayers(players);
        // distribute 7 cards to players
        System.out.println("table deck size 1: "+table.getCommonDeck().size());
        for(Player player:players){
            List<Card> cards = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                cards.add(takeRandomCard(table.getCommonDeck()));
            }
            player.setDeck(cards);
        }
        System.out.println("table deck size 2: "+table.getCommonDeck().size());
        // set active card
        table.setActivecard(table.getCommonDeck().get(0));
        table.getCommonDeck().remove(0);
        System.out.println("table deck size 3: "+table.getCommonDeck().size());
        // set active color
        table.setActiveColor(table.getActivecard().getColorType());
        // set active player index to 0
        table.setActivePlayerIndex(0);

    }

    private static Card takeRandomCard(List<Card> cards){
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

    private static List<Card> getFullDeck(){
        List<Card> fullDeck = new ArrayList<>();

        ColorType[] colorTypes = {ColorType.GREEN,ColorType.YELLOW,ColorType.BLUE,ColorType.RED};

        for(ColorType colorType:colorTypes){
            addNumberCardsWithColor(fullDeck,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.SKIP,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.SKIP,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.DRAW2,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.DRAW2,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.WILDDRAW4,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.REVERSE,colorType);
            addSpecialCardsWithTypeAndColor(fullDeck,CardType.REVERSE,colorType);

            addSpecialCardsWithTypeAndColor(fullDeck,CardType.WILD,colorType);

        }

//        int i = 0;
//        for (Card card:fullDeck){
//            i++;
//            System.out.println(i+". card type: "+card.getCardType().toString()+" color: "+card.getColorType().toString()+" number: "+card.getNumber());
//        }
//
//        System.out.println("---------------------------------------------------------------------------------------------");
//
//        Collections.shuffle(fullDeck);
//        i = 0;
//        for (Card card:fullDeck){
//            i++;
//            System.out.println(i+". card type: "+card.getCardType().toString()+" color: "+card.getColorType().toString()+" number: "+card.getNumber());
//        }

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

}
