package ua.nure.wordle.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.nure.wordle.dto.GameDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.UserGame;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGameStart(Long gameId, String word) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, word);
    }
}

