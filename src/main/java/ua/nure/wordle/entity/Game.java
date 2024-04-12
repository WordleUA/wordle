package ua.nure.wordle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nure.wordle.entity.Enum.GameStatus;

import java.security.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_status", nullable = false, columnDefinition = "ENUM")
    @Enumerated(EnumType.STRING)
    private GameStatus game_status;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "started_at")
    private Timestamp started_at;

    @Column(name = "ended_at")
    private Timestamp ended_at;
}
