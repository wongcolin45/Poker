package com.poker.app.game;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PlayerRotation {

    private BitSet active;

    private int current;

    public PlayerRotation(int numPlayers) {
        active = new BitSet(numPlayers);
        active.set(0, numPlayers); // set bits 0 -> numsPlayers -1 to true
        current = 0;
    }

    public PlayerRotation(int numPlayers, int current) {
        this(numPlayers);
        this.current = current;
    }

    public void fold(int playerId) {
        active.clear(playerId);  // Mark the player as folded
    }

    public void next() {
        int next = active.nextSetBit(current + 1); // gets next player after current player
        if (next == -1) { // if not player after current look at start
            next = active.nextSetBit(0); // 0 is from start
        }
        current = next;
    }

    public int getNext(int turn) {
        int next = active.nextSetBit(turn + 1); // gets next player after current player
        if (next == -1) { // if not player after current look at start
            next = active.nextSetBit(0); // 0 is from start
        }
        return next;
    }

    public boolean singlePlayer() {
        return active.cardinality() == 1;
    }

    public int getTurn() {
        return current;
    }

    private PlayerRotation(BitSet active, int current) {
        this.active = active;
        this.current = current;
    }

    public int getActiveCount() {
        return active.cardinality();
    }

    public List<Integer> getActiveIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < active.length(); i++) {
            if (active.get(i)) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    public int size() {
        return active.length();
    }

    public PlayerRotation copy() {
        return new PlayerRotation(active, current);
    }
}
