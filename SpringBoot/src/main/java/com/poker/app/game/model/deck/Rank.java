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

    public String getSymbol() {
        if (value == 14) {
            return "A";
        } else if (value == 13) {
            return "K";
        } else if (value == 12) {
            return "Q";
        } else if (value == 11) {
            return "J";
        }
        return String.valueOf(value);
    }
}
