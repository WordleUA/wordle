package ua.nure.wordle.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.nure.wordle.dto.request.GameEndedSocketRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.entity.enums.PlayerStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGameStart(ConnectGameResponse connectGameResponse) {
        String destination = "/topic/game/" + connectGameResponse.getGameId();
        messagingTemplate.convertAndSend(destination, connectGameResponse);
    }

    public void notifyGameEnded(PlayerStatus playerStatus, Long gameId) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, playerStatus);
    }
}

