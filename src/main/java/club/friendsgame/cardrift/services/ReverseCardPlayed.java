package club.friendsgame.cardrift.services;

import club.friendsgame.cardrift.models.Table;
import org.springframework.stereotype.Service;

@Service
public interface ReverseCardPlayed extends CardPlayed{
    void updateIsReversed(Table table);
}
