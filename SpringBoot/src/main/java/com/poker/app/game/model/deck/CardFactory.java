package com.poker.app.game.model.deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardFactory {

    private static final Map<String, Card> CARD_MAP = new HashMap<>();

    private static final Map<String, String> STRING_CARD_MAP = new HashMap<>();

    private static final Map<String, Rank> RANK_MAP = new HashMap<>() {{
        put("A", Rank.ACE);
        put("2", Rank.TWO);
        put("3", Rank.THREE);
        put("4", Rank.FOUR);
        put("5", Rank.FIVE);
        put("6", Rank.SIX);
        put("7", Rank.SEVEN);
        put("8", Rank.EIGHT);
        put("9", Rank.NINE);
        put("10", Rank.TEN);
        put("J", Rank.JACK);
        put("Q", Rank.QUEEN);
        put("K", Rank.KING);
    }};


    private static final Map<String, Suit> SUIT_MAP = Map.of(
            "♣" , Suit.CLUBS,
            "♦" , Suit.DIAMONDS,
            "♥" , Suit.HEARTS,
            "♠" , Suit.SPADES
    );

    static {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                Card card = new Card(rank, suit);
                CARD_MAP.put(rank + "-" + suit, card);
            }
        }
    }

    public static Card getCard(Rank rank, Suit suit) {
        return CARD_MAP.get(rank + "-" + suit);
    }

    public static Card getCard(String cardString) {
        String suitChar = cardString.substring(cardString.length() - 1); // last char
        String rankPart = cardString.substring(0, cardString.length() - 1); // everything else
        Rank rank = RANK_MAP.get(rankPart);
        Suit suit = SUIT_MAP.get(suitChar);
        if (rank == null || suit == null) {
            throw new IllegalArgumentException("Invalid card string: " + cardString);
        }
        return getCard(rank, suit);
    }


    public static List<Card> getCards(List<String> cardStrings) {
        List<Card> cards = new ArrayList<>();
        for (String cardString : cardStrings) {
            cards.add(getCard(cardString));
        }
        return cards;
    }



}
