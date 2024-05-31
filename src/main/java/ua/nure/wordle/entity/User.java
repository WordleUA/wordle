package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @ColumnDefault("nextval('wordle_uzmi.user_id_seq'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Size(max = 45)
    @NotNull
    @Column(name = "password_hash", nullable = false, length = 45)
    private String passwordHash;

    @Size(max = 6)
    @NotNull
    @ColumnDefault("PLAYER")
    @Column(name = "role", nullable = false, length = 6)
    private String role;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_win_count", nullable = false)
    private Integer gameWinCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_lose_count", nullable = false)
    private Integer gameLoseCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_count", nullable = false)
    private Integer gameCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "coins_total", nullable = false)
    private Integer coinsTotal;

}