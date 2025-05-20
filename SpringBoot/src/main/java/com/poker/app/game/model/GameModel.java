package com.poker.app.game.model;

import com.poker.app.game.Round;
import com.poker.app.game.PlayerRotation;
import com.poker.app.game.model.player.Move;
import java.util.Arrays;



public class GameModel implements ViewableModel {

    private static final int STARTING_CHIPS = 1000;

    private int smallBlindIndex;

    private final int[] playerChips;

    private final PlayerRotation rotation;

    private Round round;

    public GameModel(int players) {
        smallBlindIndex = 0;
        playerChips = new int[players];
        Arrays.fill(playerChips, STARTING_CHIPS);
        rotation = new PlayerRotation(players, 0);
    }

    public void startRound() {
        round = new Round(this, rotation.getTurn(), 1, 2);
    }

    public void move(Move move) {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot make any moves");
        }
        if (round.isOver()) {
            throw new IllegalStateException("Round is over, you cannot make any more moves");
        }
        round.move(move);
    }

    public void endRound() {
        if (!round.isOver()) {
            throw new IllegalStateException("Round is over, you cannot end it again");
        }
        smallBlindIndex = rotation.getNext(smallBlindIndex);
        round = null;
    }

    public boolean isRoundOver() {
        return round != null && round.isOver();
    }


    @Override
    public int getPlayerChips(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerChips.length) {
            throw new IllegalArgumentException("Invalid player index: " + playerIndex);
        }
        return playerChips[playerIndex];
    }

    public PlayerRotation getRotation() {
        return rotation;
    }

    public int getPlayerCount() {
        return rotation.getActiveCount();
    }

    public boolean isGameOver() {
        return rotation.singlePlayer();
    }

    public int getWinner() {
        if (!isGameOver()) {
            throw new IllegalStateException("Game is not over, you cannot get the winner");
        }
        return getPlayerChips(rotation.getTurn());
    }



    public int[] getPlayerChips() {
        return playerChips;
    }




}





