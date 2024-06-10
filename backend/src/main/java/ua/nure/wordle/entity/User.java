package ua.nure.wordle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @ColumnDefault("nextval('wordle_uzmi.user_id_seq'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "login", nullable = false, length = 45)
    private String login;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password_hash", nullable = false)
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
    private Long gameWinCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_lose_count", nullable = false)
    private Long gameLoseCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "game_count", nullable = false)
    private Long gameCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "coins_total", nullable = false)
    private Long coinsTotal;

    @ColumnDefault("false")
    @Column(name = "is_enabled", nullable = false)
    @Getter(AccessLevel.NONE)
    private Boolean isEnabled = false;

    @Column(name = "confirmation_code", unique = true)
    @Size(max = 64)
    private String confirmationCode;

    @Column(name = "password_reset_code", unique = true)
    @Size(max = 64)
    private String passwordResetCode;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public String getPassword() {
        return getPasswordHash();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getIsBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}