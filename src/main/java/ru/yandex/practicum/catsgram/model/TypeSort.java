package ru.yandex.practicum.catsgram.model;

public enum TypeSort {
    ASC,
    DESC;

    public static TypeSort of(String value) {
        return switch (value.toLowerCase()) {
            case "asc", "ascending" -> ASC;
            default -> DESC;
        };
    }
}
