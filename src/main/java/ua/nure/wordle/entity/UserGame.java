package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Lob
    @Column(name = "player_status")
    @Enumerated(EnumType.STRING)
    private PlayerStatus playerStatus;

    @Column(name = "is_game_over")
    private Boolean isGameOver;

    @Size(max = 6)
    @Column(name = "word", length = 6)
    private String word;

    @Column(name = "attempts")
    private Integer attempts;

}