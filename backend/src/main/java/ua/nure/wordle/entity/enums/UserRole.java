package ua.nure.wordle.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    PLAYER("PLAYER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
