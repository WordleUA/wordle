package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum PlayerStatus {
    WIN("WIN"),
    LOSE("PLAYER"),
    DRAW("DRAW");

    private final String playerStatus;

    PlayerStatus(String playerStatus) {
        this.playerStatus = playerStatus;
    }
}
