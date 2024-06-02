package ua.nure.wordle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.wordle.dto.request.RegisterRequest;
import ua.nure.wordle.dto.response.LoginResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.UserRole;
import ua.nure.wordle.exception.JwtTokenException;
import ua.nure.wordle.security.JwtService;
import ua.nure.wordle.service.interfaces.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                password
        ));

        User user = (User) userService.userDetailsService().loadUserByUsername(email);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, user.getRole());
    }

    public LoginResponse register(RegisterRequest request) {
        User user = saveUser(request);
        return login(user.getEmail(), request.getPassword());
    }

    private User saveUser(RegisterRequest request) {
        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(String.valueOf(UserRole.PLAYER))
                .isBanned(false)
                .gameWinCount(0L)
                .gameLoseCount(0L)
                .gameCount(0L)
                .coinsTotal(0L)
                .build();
        return userService.create(user);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken))
            throw new JwtTokenException("Only refresh tokens are allowed");

        String email = jwtService.extractEmail(refreshToken);
        User user = (User) userService.userDetailsService().loadUserByUsername(email);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(newAccessToken, newRefreshToken, user.getRole());
    }
}
