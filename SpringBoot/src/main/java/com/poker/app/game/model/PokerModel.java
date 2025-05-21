package com.poker.app.game.model;

import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.player.Move;
import java.util.Arrays;
import java.util.List;


public class PokerModel implements ViewableModel {

    private static final int STARTING_CHIPS = 1000;

    private int smallBlindIndex;

    private final PlayerRotation rotation;

    private final PotManager potManager;

    private Round round;

    public PokerModel(int players, int startingChips) {
        smallBlindIndex = 0;
        int[] playerChips = new int[players];
        Arrays.fill(playerChips, startingChips);
        rotation = new PlayerRotation(players, 0);
        potManager = new PotManager(playerChips);
    }

    public PokerModel(int players) {
        this(players, STARTING_CHIPS);
    }
    
    public void startRound() {
        round = new Round(this, rotation.getTurn(), 0, 1, potManager);
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
        List<Integer> winners = round.getWinner();
        potManager.payoutWinners(winners);
    }

    public PlayerRotation getRotation() {
        return rotation;
    }

    @Override
    public boolean roundNotStarted() {
        return round == null;
    }

    @Override
    public boolean roundInProgress() {
        return round != null && !round.isOver();
    }

    @Override
    public int getTurn() {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot get the turn");
        }
        return round.getTurn();
    }

    @Override
    public int getBettingRound() {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot get the betting round");
        }
        return round.getBettingRound();
    }

    @Override
    public int[] getPlayerChips() {
        return potManager.getPlayerChips();
    }

    @Override
    public int[] getPlayerBets() {
        return potManager.getPlayerBets();
    }
    
    @Override
    public List<Integer> getActivePlayers() {
        return round.getActivePlayers();
    }

    @Override
    public List<Card> getCommunityCards() {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot get community cards");
        }
        return round.getCommunityCards();
    }

    @Override
    public List<Card> getPlayerCards(int playerIndex) {
        if (round == null) {
            throw new IllegalStateException("Round is not started, you cannot get player cards");
        }
        return round.getPlayerCards(playerIndex);
    }

    @Override
    public boolean isGameOver() {
        return rotation.singlePlayer();
    }


}





