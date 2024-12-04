package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant;

public enum Answer {
    O("O"),
    X("X");

    private final String value;

    Answer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Answer fromString(String value) {
        for (Answer answer : Answer.values()) {
            if (answer.value.equalsIgnoreCase(value)) {
                return answer;
            }
        }
        throw new IllegalArgumentException("Invalid answer value: " + value);
    }
}