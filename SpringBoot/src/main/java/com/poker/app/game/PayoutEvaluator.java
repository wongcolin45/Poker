package com.poker.app.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayoutEvaluator {




    public static Map<Integer, Integer> getPayoutMap(List<Integer> winners, Map<Integer, Integer> playerBets) {

        return null;
//        // consolidate pot:
//        int totalPot = 0; // total money in pot
//        Map<Integer, Integer> payouts = new HashMap<>();
//        for (Map.Entry<Integer, Integer> entry : playerBets.entrySet()) {
//            payouts.put(entry.getKey(), -1 * entry.getValue());
//            totalPot += entry.getValue();
//        }
//
//        // Check if all bets are equal
//        if (playerBets.values().stream().distinct().count() <= 1) {
//            for (int i : winners) {
//                payouts.put(i, payouts.get(i) + totalPot / winners.size());
//            }
//        }
//
//
//        // Key = bet amount
//        // Value = List of players who bet that much
//        Map<Integer, List<Integer>> pots = new HashMap<>();
//        for (int i : winners) {
//            if (!pots.containsKey(playerBets.get(i))) {
//                pots.put(playerBets.get(i), new ArrayList<>());
//            }
//            pots.get(playerBets.get(i)).add(i);
//        }
//
//
//
//        List<Integer> sortedBets = new ArrayList<>(pots.keySet());
//        sortedBets.sort((o1, o2) -> o2.compareTo(o1));
//
//        for (int i = 0; i < sortedBets.size(); i++) {
//            int smallPot = sortedBets.get(i) * (sortedBets.size() - i);
//
//
//
//        }
    }







}
