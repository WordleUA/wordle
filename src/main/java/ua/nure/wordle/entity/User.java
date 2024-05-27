package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ua.nure.wordle.entity.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned;

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
