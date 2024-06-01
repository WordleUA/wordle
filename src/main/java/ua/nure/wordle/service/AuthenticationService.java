package ua.nure.wordle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.wordle.dto.request.LoginRequest;
import ua.nure.wordle.dto.response.LoginResponse;
import ua.nure.wordle.entity.User;
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


    public LoginResponse signIn(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        User user = (User) userService.userDetailsService().loadUserByUsername(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, user.getRole());
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
