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

    @Enumerated(EnumType.STRING)
    @Column(name = "player_status")
    private PlayerStatus playerStatus;

    @Size(max = 6)
    @ColumnDefault("NULL")
    @Column(name = "word", length = 6)
    private String word;

    @Column(name = "attempts")
    private Integer attempts;

}