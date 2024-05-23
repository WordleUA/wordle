package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum GameStatus {
    COMPLETE("COMPLETE"),
    SEARCH("SEARCH"),
    IN_PROGRESS("IN_PROGRESS"),
    CANCELED("CANCELED");

    private final String name;

    GameStatus(String name) {
        this.name = name;
    }
}
