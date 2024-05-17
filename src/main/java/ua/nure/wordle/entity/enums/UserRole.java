package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    PLAYER("PLAYER");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }
}
