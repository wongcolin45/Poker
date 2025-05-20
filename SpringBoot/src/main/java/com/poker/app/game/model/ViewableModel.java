package com.poker.app.game.model;


import com.poker.app.game.model.deck.Card;

import java.util.List;

public interface ViewableModel {

    int getPlayerChips(int playerIndex);

    List<Card> getCommunityCards();

}
