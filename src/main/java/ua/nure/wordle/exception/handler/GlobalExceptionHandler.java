package ua.nure.wordle.exception.handler;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.nure.wordle.dto.response.MessageResponse;
import ua.nure.wordle.exception.JwtTokenException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<MessageResponse> handleJwtTokenException(JwtTokenException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
