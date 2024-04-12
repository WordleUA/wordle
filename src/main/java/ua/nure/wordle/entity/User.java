package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

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
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @NotNull
    @ColumnDefault("PLAYER")
    @Lob
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_banned", nullable = false)
    private Byte isBanned;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_win_count", nullable = false)
    private Integer gameWinCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_count", nullable = false)
    private Integer gameCount;

}