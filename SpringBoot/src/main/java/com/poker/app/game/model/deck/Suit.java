package com.poker.app.game.model.deck;

public enum Suit {
    CLUBS("♣"), DIAMONDS("♦"), HEARTS("♥"), SPADES("♠");

    private final String symbol;

    private Suit(String symbol) {
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }

}
