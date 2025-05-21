package com.poker.app.game.model;

import com.poker.app.game.model.player.Action;
import com.poker.app.game.model.player.Move;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoundTest {

    private final PokerModel model;
    private final Round round;

    public RoundTest() {
        model = new PokerModel(2, 500);
        round = new Round(model, 0, 0, 1, new PotManager(new int[]{500, 500}));
    }

    @Test
    public void testQuickRound() {
        round.move(new Move(Action.ALL_IN));
        round.move(new Move(Action.CALL));
        Assertions.assertTrue(round.isOver());
    }

    @Test
    public void testAllCall() {
        for (int i = 0; i < 5; i++) {
            round.move(new Move(Action.RAISE, 1));
            round.move(new Move(Action.CALL));
        }
        Assertions.assertTrue(round.isOver());
    }

}