package com.poker.app.game.controller;

import com.poker.app.game.model.GameModel;
import com.poker.app.game.model.Round;
import com.poker.app.game.model.player.Action;
import com.poker.app.game.model.player.Move;

import java.util.Scanner;

public class GameController {

    private final GameModel model;

    private final Scanner scanner;

    public GameController() {
        model = new GameModel(2);
        scanner = new Scanner(System.in);

        while (!model.isGameOver()) {
            model.startRound();
            while (!model.isRoundOver()) {
                Move move = getMove();
                model.move(move);
            }
            model.endRound();
        }

    }

    private Move getMove() {
        System.out.println("Enter action CALL(C), CHECK(M), RAISE(R), ALL_IN(A), FOLD(F)");
        String action = scanner.nextLine();
        if (action.equals("C")) {
            return new Move(Action.CALL);
        }
        if (action.equals("M")) {
            return new Move(Action.CHECK);
        }
        if (action.equals("R")) {
            System.out.println("Enter raise amount");
            int raiseAmount = scanner.nextInt();
            return new Move(Action.RAISE, raiseAmount);
        }
        if (action.equals("A")) {
            return new Move(Action.ALL_IN);
        }
        return new Move(Action.FOLD);
    }

}
