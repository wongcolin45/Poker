package com.poker.app.game.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PotManagerTest {

    private final Random rand = new Random(42);

    private PotManager createPotManager(int chips, int players) {
        int[] playerChips = new int[players];
        Arrays.fill(playerChips, chips);
        return new PotManager(playerChips, new PlayerRotation(players, 0));
    }

    private void testAllIn1WinnerSameChips(int chips, int players) {
        PotManager potManager = createPotManager(100, players);
        for (int i = 0; i < players; i++) {
            potManager.allIn(i);
        }
        int winner = rand.nextInt(players);
        int[] expected = new int[players];
        expected[winner] = chips * players;
        Assertions.assertArrayEquals(expected, potManager.getPayouts(List.of(winner)));
    }

    @Test
    public void testAllIn1WinnerSameChips() {
        for (int i = 2; i <= 10; i++) {
            testAllIn1WinnerSameChips(100, i);
        }
    }






}