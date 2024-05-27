package ua.nure.wordle.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.nure.wordle.dto.GameEndedDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGameStart(Long gameId, String word) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, word);
    }

    public void notifyGameEnded(List<GameEndedDTO> results, Long gameId) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, results);
    }
}

