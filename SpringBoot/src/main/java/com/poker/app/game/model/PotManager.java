package com.poker.app.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PotManager {

    private int callAmount;

    private final int[] playerChips;

    private final int[] playerBets;

    private final PlayerRotation rotation;

    public PotManager(int[] playerChips, PlayerRotation rotation) {
        callAmount = 0;
        this.playerChips = Objects.requireNonNull(playerChips).clone();
        this.playerBets = new int[playerChips.length];
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

    public int[] getPayouts(List<Integer> winners) {
        Objects.requireNonNull(winners);
        int[] payouts = new int[playerBets.length];
        if (winners.size() == 1 || winners.stream().distinct().count() <= 1) {
            int totalPot = 0;
            for (int i = 0; i < playerBets.length; i++) {
                totalPot += playerBets[i];
            }
            for (int winner : winners) {
                payouts[winner] = totalPot / winners.size();
            }
            return payouts;
        }
        List<Integer> sortedWinners = new ArrayList<>(winners);
        sortedWinners.sort((a, b) -> Integer.compare(playerBets[a], playerBets[b]));
        int previousBet = 0;
        for (int i = 0; i < winners.size(); i++) {
            int payout = (playerBets[i] - previousBet) * (winners.size() - i);
            for (int j = i; j < winners.size(); j++) {
                payouts[sortedWinners.get(j)] += payout;
            }
        }
        return payouts;
    }








}
