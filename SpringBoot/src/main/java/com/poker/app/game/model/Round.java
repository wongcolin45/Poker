package com.poker.app.game.model;

import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.deck.Deck;
import com.poker.app.game.model.evaluator.HandEvaluator;
import com.poker.app.game.model.player.Action;
import com.poker.app.game.model.player.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the state of a round of poker
 */
public class Round {

    private int turn;

    private int bettingRound = 1;

    private int start;

    private boolean loopMade;

    private final List<Card> communityCards;

    private final Map<Integer, List<Card>> playerHands;

    private final PlayerRotation rotation;

    private final Deck deck;

    private final PotManager potManager;

    private final PokerModel model;

    private void endBettingRound() {
        if (bettingRound == 1) {
            for (int i = 0; i < 3; i++) {
                communityCards.add(deck.draw());
            }
        } else if (bettingRound == 2 || bettingRound == 3) {
            communityCards.add(deck.draw());
        }
        bettingRound++;
    }

    public Round(PokerModel model, int start, int smallBlind, int bigBlind, PotManager potManager) {
        if (model.getRotation().singlePlayer()) {
            throw new IllegalArgumentException("Single player round not supported");
        }
        int[] chips =  potManager.getPlayerChips();
        if (chips[smallBlind] < smallBlind || chips[bigBlind] < bigBlind) {
            throw new IllegalArgumentException("Current big and small blinds don't have enough chips to start round");
        }
        rotation = model.getRotation().copy();
        deck = new Deck();
        this.model = model;
        this.potManager = potManager;
        this.potManager.initializeRound(rotation);
        communityCards = new ArrayList<>();
        playerHands = new HashMap<>();
        for (int i = 0; i < rotation.getActiveCount(); i++) {
            playerHands.put(rotation.getTurn(), deck.drawHand());
            rotation.next();
        }
        turn = start;
        this.start = start;
        loopMade = false;
    }

    public void move(Move move) {
        if (isOver()) {
            throw new IllegalStateException("Round is over, you cannot make any more moves");
        }
        switch (move.getAction()) {
            case Action.FOLD:
                rotation.fold(turn);
                break;
            case Action.CALL:
                potManager.call(turn);
            case Action.RAISE:
                potManager.raise(turn, move.getRaiseAmount());
                break;
            default:
                potManager.allIn(turn);
                break;
        }
        if (rotation.getNext(turn) == start) {
            loopMade = true;
        }
        if (move.getAction() == Action.FOLD) {
            start = rotation.getNext(start);
        }
        if (loopMade && !potManager.unmatchedBet()) {
            loopMade = false;
            endBettingRound();
        }
        turn = rotation.getNext(turn);
    }

    public int getTurn() {
        return turn;
    }

    public int getBettingRound() {
        return bettingRound;
    }

    public boolean isOver() {
        return potManager.playersAllIn() || rotation.singlePlayer() || bettingRound > 5  || potManager.playersAllIn();
    }

    public List<Integer> getActivePlayers() {
        return rotation.getActiveIndexes();
    }

    public List<Integer> getWinner() {
        if (!isOver()) {
            throw new IllegalStateException("Round is not over, you cannot get the result");
        }
        while (communityCards.size() < 5) {
            communityCards.add(deck.draw());
        }
        return HandEvaluator.getWinningPlayer(playerHands, communityCards);
    }

    public List<Card> getPlayerCards(int playerIndex) {
        return List.copyOf(playerHands.get(playerIndex));
    }

    public List<Card> getCommunityCards() {
        return new ArrayList<>(communityCards);
    }








}
