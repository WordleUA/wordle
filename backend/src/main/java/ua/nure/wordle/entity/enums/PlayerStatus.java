package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum PlayerStatus {
    WIN("WIN"),
    LOSE("PLAYER"),
    DRAW("DRAW"),
    WAITING("WAITING"),
    IN_GAME("IN_GAME");

    private final String name;

    PlayerStatus(String name) {
        this.name = name;
    }
}
