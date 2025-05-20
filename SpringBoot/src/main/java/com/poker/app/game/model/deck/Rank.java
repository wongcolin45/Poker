package com.poker.app.game.model.deck;

public enum Rank {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13), ACE(14);

    private final int value;

    private Rank(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString() {
        if (this == TEN) {
            return "10";
        }
        return name().substring(0, 1).toUpperCase();
    }
}
