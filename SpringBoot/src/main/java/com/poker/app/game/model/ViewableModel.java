package com.poker.app.game.model;


import com.poker.app.game.model.deck.Card;

import java.util.List;

public interface ViewableModel {

    boolean roundNotStarted();

    boolean roundInProgress();

    boolean isGameOver();

    int getTurn();

    int getBettingRound();

    int[] getPlayerChips();

    int[] getPlayerBets();

    List<Integer> getActivePlayers();

    List<Card> getCommunityCards();

    List<Card> getPlayerCards(int playerIndex);

}
