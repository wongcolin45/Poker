package com.poker.app.game.model.deck;

public record Card(Rank rank, Suit suit) implements Comparable<Card> {

    public Card copy() {
        return new Card(rank, suit);
    }

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card other) {
            return rank == other.rank && suit == other.suit;
        }
        return false;
    }

    @Override
    public int compareTo(Card card) {
        return this.rank.getValue() - card.rank.getValue();
    }


}
