package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ua.nure.wordle.entity.enums.GameStatus;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "game", schema = "wordle_uzmi")
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("SEARCH")
    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "game_status", nullable = false)
    private GameStatus gameStatus;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "started_at")
    private Timestamp startedAt;

    @Column(name = "ended_at")
    private Timestamp endedAt;

    @OneToMany(mappedBy = "game")
    private Set<UserGame> userGames = new LinkedHashSet<>();

}