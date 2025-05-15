package com.poker.app.game.model;

public enum Rank {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9),
    JACK(10), QUEEN(11), KING(12), ACE(13);

    private final int value;

    private Rank(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
