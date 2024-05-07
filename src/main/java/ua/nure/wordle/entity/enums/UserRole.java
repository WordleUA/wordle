package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    PLAYER("PLAYER");
    private String name;
    private UserRole(String name) {
        this.name = name;
    }
}
