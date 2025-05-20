package com.poker.app.game;

import com.poker.app.game.model.CardUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PotManager {

    private int callAmount;

    private int[] playerChips;

    private int[] playerBets;

    private final PlayerRotation rotation;

    public PotManager(int[] playerChips, PlayerRotation rotation) {
        callAmount = 0;
        playerBets = null;
        this.playerChips = Objects.requireNonNull(playerChips).clone();
        this.rotation = Objects.requireNonNull(rotation);
    }

    public void raise(int player, int raiseAmount) {
        if (raiseAmount < 0) {
            throw new IllegalArgumentException("Raise amount cannot be negative");
        }
        if (player >= playerBets.length || player < 0) {
            throw new IllegalArgumentException("Player " + player + " does not have associated chips");
        }
        int chipsNeeded = callAmount - playerBets[player] + raiseAmount;
        if (chipsNeeded > playerChips[player]) {
            throw new IllegalStateException("Player " + player + " has insufficient chips to raise");
        }
        playerBets[player] = callAmount + raiseAmount;
        playerChips[player] -= chipsNeeded;
        callAmount += raiseAmount;
    }

    public void call(int player) {
        if (player >= playerBets.length || player < 0) {
            throw new IllegalArgumentException("Player " + player + " does not have associated chips");
        }
        int chipsNeeded = callAmount - playerBets[player];
        if (chipsNeeded > playerChips[player]) {
            throw new IllegalStateException("Player " + player + " has insufficient chips to call");
        }
        playerBets[player] = callAmount;
        playerChips[player] -= chipsNeeded;
    }

    public void allIn(int player) {
        if (player >= playerBets.length || player < 0) {
            throw new IllegalArgumentException("Player " + player + " does not have associated chips");
        }
        playerBets[player] += playerChips[player];
        playerChips[player] = 0;

        if (playerBets[player] > callAmount) {
            callAmount = playerBets[player];
        }
    }

    public boolean unmatchedBet() {
        for (int i : rotation.getActiveIndexes()) {
            if (playerBets[i] < callAmount && playerChips[i] > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean playersAllIn() {
        for (int i : rotation.getActiveIndexes()) {
            if (playerChips[i] > 0) {
                return false;
            }
        }
        return true;
    }

    public int getPlayerBet(int player) {
        return playerBets[player];
    }

    public int getCallAmount() {
        return callAmount;
    }








}
