package com.poker.app.game.evaluator;

public enum HandType {
    ROYAL_FLUSH(9, 0),
    STRAIGHT_FLUSH(8, 0),
    FOUR_OF_A_KIND(7, 1),
    FULL_HOUSE(6, 0),
    FLUSH(5, 0),
    STRAIGHT(4, 0),
    THREE_OF_A_KIND(3, 2),
    TWO_PAIR(2, 1),
    ONE_PAIR(1, 3),
    HIGH_CARD(0, 5);

    private final int value;

    private final int kickers;

    private HandType(int value, int kickers) {
        this.value = value;
        this.kickers = kickers;
    }

    public int getValue() {
        return value;
    }

    public int kickers() {
        return kickers;
    }

    public boolean hasPairs() {
        return (this == ONE_PAIR || this == TWO_PAIR || this == THREE_OF_A_KIND || this == FOUR_OF_A_KIND);
    }

    @Override
    public String toString() {
        return name();
    }
}
