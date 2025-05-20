package com.poker.app.game.model;

import com.poker.app.game.model.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class CardUtils {



    public static List<Card> getCardListCopy(List<Card> cards) {
        return new ArrayList<>(cards);
//        List<Card> cardsCopy = new ArrayList<>();
//        for (Card card : cards) {
//            cardsCopy.add(card.copy());
//        }
//        return cardsCopy;
    }

    public static void printCardList(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i+1)+": "+cards.get(i));
        }
    }

    public static Card[] copyCards(Card[] cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }
        Card[] copy = new Card[cards.length];
        System.arraycopy(cards, 0, copy, 0, cards.length);
        return copy;
    }
}
