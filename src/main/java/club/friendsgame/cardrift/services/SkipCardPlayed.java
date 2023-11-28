package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.Table;
import org.springframework.stereotype.Service;

@Service
public interface SkipCardPlayed extends CardPlayed{
    void updateCurrentPlayer2Times(Table table);
}
