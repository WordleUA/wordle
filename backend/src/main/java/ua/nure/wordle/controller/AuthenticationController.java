package ua.nure.wordle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.request.LoginRequest;
import ua.nure.wordle.dto.request.RegisterRequest;
import ua.nure.wordle.dto.response.LoginResponse;
import ua.nure.wordle.dto.response.MessageResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.event.OnRegistrationCompleteEvent;
import ua.nure.wordle.exception.EmailAlreadyExistsException;
import ua.nure.wordle.service.AuthenticationService;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<LoginResponse> loginUser(@ModelAttribute LoginRequest loginRequest) {
        LoginResponse response = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<MessageResponse> registerUser(@ModelAttribute RegisterRequest registerDTO) {
        User registered = authenticationService.register(registerDTO);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
        return new ResponseEntity<>(new MessageResponse("User registered"), HttpStatus.CREATED);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String refreshToken = getToken(authorizationHeader);
        LoginResponse response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-registration")
    public ResponseEntity<MessageResponse> confirmRegistration(@RequestBody String code) {
        if (authenticationService.confirmRegistration(code)) {
            return new ResponseEntity<>(new MessageResponse("Registration confirmed"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Registration not confirmed"), HttpStatus.BAD_REQUEST);
    }

    private static String getToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
