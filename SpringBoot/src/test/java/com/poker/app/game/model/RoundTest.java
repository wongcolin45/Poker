package com.poker.app.game.model;

import com.poker.app.game.model.player.Action;
import com.poker.app.game.model.player.Move;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    private final GameModel model;
    private final Round round;

    public RoundTest() {
        model = new GameModel(2, 500);
        round = new Round(model, 0, 0, 1);
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