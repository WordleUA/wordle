package ua.nure.wordle.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGameStart(Long gameId) {
        String destination = "/topic/game/start/" + gameId;
        messagingTemplate.convertAndSend(destination, "The game has started!");
    }
}

