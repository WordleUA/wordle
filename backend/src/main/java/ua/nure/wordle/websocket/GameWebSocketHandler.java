package ua.nure.wordle.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.dto.response.EndGameResponse;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGameStart(ConnectGameResponse connectGameResponse) {
        String destination = "/topic/game/" + connectGameResponse.getGameId();
        messagingTemplate.convertAndSend(destination, connectGameResponse);
    }

    public void notifyGameEnded(EndGameResponse endGameResponse) {
        String destination = "/topic/game/" + endGameResponse.getGameId();
        messagingTemplate.convertAndSend(destination, endGameResponse.getPlayerStatus());
    }
}

