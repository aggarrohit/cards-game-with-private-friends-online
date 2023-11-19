package club.friendsgame.cardrift.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    private int id;
    private String name;
    private List<Card> deck;
}
