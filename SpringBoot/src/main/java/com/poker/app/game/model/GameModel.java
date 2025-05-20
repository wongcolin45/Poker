package com.poker.app.game.model;

import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.player.Move;
import java.util.Arrays;
import java.util.List;


public class GameModel implements ViewableModel {

    private static final int STARTING_CHIPS = 1000;

    private int smallBlindIndex;

    private int[] playerChips;

    private final PlayerRotation rotation;

    private Round round;

    public GameModel(int players) {
        smallBlindIndex = 0;
        playerChips = new int[players];
        Arrays.fill(playerChips, STARTING_CHIPS);
        rotation = new PlayerRotation(players, 0);
    }


    public GameModel(int players, int startingChips) {
        smallBlindIndex = 0;
        playerChips = new int[players];
        Arrays.fill(playerChips, startingChips);
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
        playerChips = round.getPayouts();
    }

    public boolean isRoundOver() {
        return round != null && round.isOver();
    }

    public PlayerRotation getRotation() {
        return rotation;
    }

    @Override
    public int getPlayerChips(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerChips.length) {
            throw new IllegalArgumentException("Invalid player index: " + playerIndex);
        }
        return playerChips[playerIndex];
    }

    @Override
    public List<Card> getCommunityCards() {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot get community cards");
        }
        return round.getCommunityCards();
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





