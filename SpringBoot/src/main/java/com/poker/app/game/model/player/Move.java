package com.poker.app.game.model.player;

public class Move {
    private final Action action;
    private final int raiseAmount;

    public Move(Action action) {
        this.action = action;
        this.raiseAmount = 0;
    }

    public Move(Action action, int raiseAmount) {
        if (raiseAmount < 0) {
            throw new IllegalArgumentException("Raise amount cannot be negative");
        }
        if (action == Action.RAISE && raiseAmount == 0) {
            throw new IllegalArgumentException("Raise action must be have raise amount greater than 0");
        }
        this.action = action;
        this.raiseAmount = raiseAmount;
    }

    public Action getAction() {
        return action;
    }

    public int getRaiseAmount() {
        return raiseAmount;
    }


}
