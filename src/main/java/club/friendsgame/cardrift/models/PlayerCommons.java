package club.friendsgame.cardrift.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlayerCommons {

    private int id;
    private String name;
    private int deckSize;
    private int rank;
}
