package com.poker.app.game;

import com.poker.app.game.model.GameModel;
import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.deck.Deck;
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

    private int startIndex;

    private boolean loopMade;

    private final List<Card> communityCards;

    private final Map<Integer, List<Card>> playerHands;

    private final PlayerRotation rotation;

    private final Deck deck;

    private final PotManager potManager;

    private final GameModel model;

    public Round(GameModel model, int startIndex, int smallBlind, int bigBlind) {
        if (model.getRotation().singlePlayer()) {
            throw new IllegalArgumentException("Single player round not supported");
        }
        if (model.getPlayerChips(turn) < smallBlind || model.getPlayerChips(turn) < bigBlind) {
            throw new IllegalArgumentException("Current big and small blinds don't have enough chips to start round");
        }
        rotation = model.getRotation().copy();
        deck = new Deck();
        this.model = model;
        communityCards = new ArrayList<>();
        playerHands = new HashMap<>();
        for (int i = 0; i < rotation.getActiveCount(); i++) {
            playerHands.put(rotation.getTurn(), deck.drawHand());
            rotation.next();
        }
        turn = startIndex;
        this.potManager = new PotManager(model.getPlayerChips(), rotation);
        this.startIndex = startIndex;
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
                potManager.call(turn);
            case Action.RAISE:
                potManager.raise(turn, move.getRaiseAmount());
                break;
            default:
                break;
        }
        if (turn > rotation.getNext(turn)) {
            loopMade = true;
        }
        if (loopMade && !potManager.unmatchedBet()) {
            loopMade = false;
            endBettingRound();
        }
        turn = rotation.getNext(turn);
    }

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

    public boolean isOver() {
        return potManager.playersAllIn() || rotation.singlePlayer()  || bettingRound > 5  || potManager.playersAllIn();
    }






}
