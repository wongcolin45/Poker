package com.poker.app.game.controller;

import com.poker.app.game.model.PokerModel;
import com.poker.app.game.model.player.Action;
import com.poker.app.game.model.player.Move;
import com.poker.app.game.view.PokerTerminalView;

import java.util.Scanner;

public class PokerTerminalController {

    private final PokerModel model;

    private final PokerTerminalView view;

    private final Scanner scanner;

    public PokerTerminalController() {
        model = new PokerModel(2);
        view = new PokerTerminalView(model);
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (!model.isGameOver()) {
            model.startRound();
            view.render();
            while (!model.roundNotStarted()) {  // Changed condition
                Move move = getMove();
                model.move(move);
                view.render();
            }
            
            model.endRound();  // Uncomment this to properly end the round
        }
    }

    private Move getMove() {
        while (true) {  // Add error handling loop
            System.out.println("Enter action CALL(C), CHECK(M), RAISE(R), ALL_IN(A), FOLD(F)");
            String action = scanner.nextLine().trim().toUpperCase();  // Add trim and toUpperCase
            
            try {
                switch (action) {
                    case "C" -> {
                        return new Move(Action.CALL);
                    }
                    case "M" -> {
                        return new Move(Action.CHECK);
                    }
                    case "R" -> {
                        System.out.println("Enter raise amount");
                        int raiseAmount = Integer.parseInt(scanner.nextLine());  // Change to nextLine for consistency
                        return new Move(Action.RAISE, raiseAmount);
                    }
                    case "A" -> {
                        return new Move(Action.ALL_IN);
                    }
                    case "F" -> {
                        return new Move(Action.FOLD);
                    }
                    default -> {
                        System.out.println("Invalid action. Please try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        PokerTerminalController controller = new PokerTerminalController();
        controller.start();
    }

}