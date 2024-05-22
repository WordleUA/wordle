package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@Entity
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
    private PlayerStatus playerStatus;

    @Size(max = 6)
    @Column(name = "word", length = 6)
    private String word;

    @NotNull
    @Column(name = "is_game_over", nullable = false)
    private Byte isGameOver;

    @NotNull
    @Column(name = "attempts", nullable = false)
    private Long attempts;

}