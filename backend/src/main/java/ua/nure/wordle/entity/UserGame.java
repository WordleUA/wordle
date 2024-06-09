package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "user_game")
public class UserGame {
    @EmbeddedId
    private UserGameId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("gameId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Size(max = 4)
    @ColumnDefault("NULL")
    @Column(name = "player_status", length = 4)
    private String playerStatus;

    @Size(max = 6)
    @ColumnDefault("NULL")
    @Column(name = "word", length = 6)
    private String word;

    @Column(name = "attempts")
    private Integer attempts;

    public void determinePlayerStatus(PlayerStatus playerStatus){
        switch (playerStatus) {
            case WIN:
                this.playerStatus = String.valueOf(PlayerStatus.LOSE);
                break;
            case LOSE:
                this.playerStatus = String.valueOf(PlayerStatus.WIN);
                break;
            default:
                this.playerStatus = String.valueOf(PlayerStatus.DRAW);
                break;
        }
    }
}