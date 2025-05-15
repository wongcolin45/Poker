package com.poker.app.game.model;

public class PokerGame {

    private boolean gameStarted;

    public PokerGame() {
        gameStarted = false;
    }

    public void start() {
        if (gameStarted) {
            throw new IllegalStateException("Game has already started");
        }
        gameStarted = true;
    }

}
