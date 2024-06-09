package ua.nure.wordle.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.service.interfaces.UserService;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = (User) userService.userDetailsService().loadUserByUsername(email);

        if (user == null || (!user.getEmail().equals(email) && !user.getUsername().equals(email)))
            throw new UsernameNotFoundException("Користувача з email " + email + " не знайдено");

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Неправильний пароль");

        if (!user.isEnabled())
            throw new DisabledException("Ваша електронна адреса не підтверджена. Перевірте пошту");

        if (Boolean.TRUE.equals(user.getIsBanned()))
            throw new LockedException("Неможливо авторизуватися. Ваш акаунт заблоковано");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
