package club.friendsgame.cardrift.mappers;

import club.friendsgame.cardrift.models.PlayerCommons;
import club.friendsgame.cardrift.models.Table;
import club.friendsgame.cardrift.models.TableCommons;

public class TableMapper {

    public static TableCommons toTableCommons(Table table){
        return TableCommons.builder()
                .id(table.getId())
                .numberOfPlayers(table.getNumberOfPlayers())
                .activePlayerIndex(table.getActivePlayerIndex())
                .activecard(table.getActivecard())
                .tableId(table.getTableId())
                .isReversed(table.isReversed())
                .activeColor(table.getActiveColor())
                .isCardDrawn(table.isCardDrawn())
                .unoSaidPhases(table.getUnoSaidPhase())
                .isGameCompleted(table.isGameCompleted())
                .playersCommons(table.getPlayers().stream().map(player -> PlayerCommons .builder()
                                                                                        .id(player.getId())
                                                                                        .name(player.getName())
                                                                                        .deckSize(player.getDeck().size())
                                                                                        .rank(player.getRank())
                                                                                        .build()).toList()
                                )
                .build();
    }

}
