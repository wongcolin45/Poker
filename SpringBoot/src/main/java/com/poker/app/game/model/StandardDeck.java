package com.poker.app.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StandardDeck {

    private final List<Card> cards;

    public StandardDeck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.removeFirst();
    }

}
