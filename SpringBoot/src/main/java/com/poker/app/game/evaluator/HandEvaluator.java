package com.poker.app.game.evaluator;

import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.deck.CardFactory;
import com.poker.app.game.model.deck.Rank;
import com.poker.app.game.model.deck.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for evaluating poker hands.
 * Broken Down into 3 main methods:
 *  - getBestMatching -> the best hand with matching cards: uses hashmap of ranks
 *  - getBestFlush -> the best flush hand: uses hashmap of suits
 *  - getBestStraight-> the best straight hand (only one type of straight)
 */
public final class HandEvaluator {

    public static class HandEvaluation {

        private final HandType hand;

        private final List<Card> cardsUsed;

        private final List<Card> kickers;

        private final List<Card> unusedCards;

        public HandEvaluation(HandType hand, List<Card> cardsUsed, List<Card> unusedCards) {
            if (cardsUsed.size() + unusedCards.size() != 7) {
                throw new IllegalArgumentException("Cards used and unused cards must be 7 cards long");
            }
            this.hand = Objects.requireNonNull(hand);
            this.cardsUsed = new ArrayList<>(Objects.requireNonNull(cardsUsed));
            List<Card> remainingCards = new ArrayList<>(Objects.requireNonNull(unusedCards));
            int kickersNeeded = Math.max(0, 5 - cardsUsed.size());
            if (kickersNeeded > 0) {
                this.kickers = Collections.unmodifiableList(getKickers(remainingCards, kickersNeeded));
                remainingCards.removeAll(kickers);
            } else {
                this.kickers = List.of();
            }
            this.unusedCards = remainingCards;


        }

        private List<Card> getKickers(List<Card> cards, int k) {
            if (k > cards.size()) {
                throw new IllegalArgumentException("k: " +k+ " must be <= cards.size(): "+cards.size());
            }
            cards.sort(Comparator.reverseOrder());
            return new ArrayList<>(cards.subList(0, k));
        }

        public boolean hasKickers() {
            return !kickers.isEmpty();
        }

        public List<Card> getKickers() {
            return new ArrayList<>(kickers);
        }

        public HandType getHand() {
            return hand;
        }

        public List<Card> getCardsUsed() {
            List<Card> cards = new ArrayList<>(cardsUsed);
            cards.addAll(getKickers());
            return cards;
        }

        public List<Card> getUnusedCards() {
            return new ArrayList<>(unusedCards);
        }
    }


    private static HandEvaluation getMatchType(List<Card> cards, Map<Rank, List<Card>> cardPairs,
                                               Rank bestRank, int bestCount, Rank secondBestRank, int secondBestCount) {
        List<Card> bestCards = cardPairs.get(bestRank);
        cards.removeAll(bestCards);
        List<Card> kickers;
        if (bestCount == 2) {
            if (secondBestCount == 1) {
                return new HandEvaluation(HandType.ONE_PAIR, bestCards, cards);
            }
            List<Card> secondBestPair = cardPairs.get(secondBestRank);
            bestCards.addAll(secondBestPair);
            cards.removeAll(secondBestPair);
            return new HandEvaluation(HandType.TWO_PAIR, bestCards, cards);
        }
        if (bestCount == 3) {
            // Full House
            if (secondBestCount == 2) {
                bestCards.addAll(cardPairs.get(secondBestRank));
                cards.removeAll(cardPairs.get(secondBestRank));
                return new HandEvaluation(HandType.FULL_HOUSE, bestCards, cards);
            }
            return new HandEvaluation(HandType.THREE_OF_A_KIND, bestCards, cards);
        }
        return new HandEvaluation(HandType.FOUR_OF_A_KIND, bestCards, cards);
    }

    /**
     * Gets the best poker hand with matching cards.
     * @param combinedCards the 7 combined cards to use
     * @param highCardEvaluation the default if no matches exist
     * @return the best poker hand with matching cards or the default if no matches exist
     */
    private static HandEvaluation getBestMatching(List<Card> combinedCards, HandEvaluation highCardEvaluation) {
        List<Card> cards = new ArrayList<>(combinedCards);
        Map<Rank, List<Card>> cardRankMap = new HashMap<>();
        // Initialize hashmap
        for (Rank rank : Rank.values()) {
            cardRankMap.put(rank, new ArrayList<>());
        }
        // Check for pairs
        for (Card card : cards) {
            cardRankMap.get(card.rank()).add(card);
        }
        // Calculate 1st and 2nd most common pairs
        Rank bestRank = Rank.ONE;
        int bestCount = cardRankMap.get(Rank.ONE).size();
        Rank secondBestRank = null;
        int secondBestCount = 0;

        for (Map.Entry<Rank, List<Card>> entry : cardRankMap.entrySet()) {
            int cardCount = entry.getValue().size();
            int rankValue = entry.getKey().getValue();
            double score = cardCount + 0.1 * rankValue;
            double secondBestScore = (secondBestRank != null) ? secondBestCount + 0.1 * secondBestRank.getValue() : 0;
            double bestScore = bestCount + 0.1 * bestRank.getValue();
            if (score > secondBestScore && score < bestScore) {
                secondBestRank = entry.getKey();
                secondBestCount = cardCount;
                continue;
            }
            if (score > bestScore) {
                secondBestRank = bestRank;
                secondBestCount = bestCount;
                bestRank = entry.getKey();
                bestCount = cardCount;
            }
        }
        if (bestCount == 1) {
            return highCardEvaluation;
        }
        return getMatchType(cards, cardRankMap, bestRank, bestCount, secondBestRank, secondBestCount);
    }


    /**
     * Gets the best poker hand with a flush.
     * @param combinedCards the 7 combined cards to use
     * @param highCardHandEvaluation the default if no flush exists
     * @return the best poker hand with a flush or the default if no flush exists
     */
    private static HandEvaluation getBestFlush(List<Card> combinedCards, HandEvaluation highCardHandEvaluation) {
        List<Card> cards = new ArrayList<>(combinedCards);
        Map<Suit, List<Card>> cardSuitMap = new HashMap<>();
        for (Suit suit : Suit.values()) {
            cardSuitMap.put(suit, new ArrayList<>());
        }
        for (Card c : cards) {
            cardSuitMap.put(c.suit(), cardSuitMap.get(c.suit())).add(c);
        }
        for (Suit suit : Suit.values()) {
            List<Card> suitCards = cardSuitMap.get(suit);


            if (suitCards.size() >= 5) {
                Collections.sort(suitCards);
                List<Card> iterSuitCards = new ArrayList<>(suitCards);
                Card ace = CardFactory.getCard(Rank.ACE, suit);
                if (suitCards.contains(ace)) {
                    iterSuitCards.addFirst(ace);
                }

                int l = iterSuitCards.size() - 1;
                List<Card> consecutiveCards = new ArrayList<>();
                consecutiveCards.add(iterSuitCards.get(l));
                l--;
                while (l >= 0 && consecutiveCards.size() < 5) {
                    int rank = iterSuitCards.get(l).rank().getValue();
                    int rankAbove = iterSuitCards.get(l+1).rank().getValue();
                    if (rank + 1 != rankAbove) {
                        consecutiveCards.clear();
                    } else {
                        consecutiveCards.add(iterSuitCards.get(l));
                    }

                    l--;

                }
                // Normal Flush
                if (consecutiveCards.size() < 5) {
                    List<Card> bestCards = suitCards.subList(suitCards.size() - 5, suitCards.size());
                    cards.removeAll(bestCards);
                    return new HandEvaluation(HandType.FLUSH, bestCards, cards);
                }
                cards.removeAll(consecutiveCards);
                HandType handType = (consecutiveCards.getFirst().rank() == Rank.ACE) ? HandType.ROYAL_FLUSH : HandType.STRAIGHT_FLUSH;

                return new HandEvaluation(handType, consecutiveCards, cards);

            }
        }
        return highCardHandEvaluation;
    }


    /**
     * Gets the best poker hand with a straight.
     * @param combinedCards the 7 combined cards to use
     * @param highCardHandEvaluation the default if no straight exists
     * @return the best poker hand with a straight or the default if no straight exists
     */
    private static HandEvaluation getBestStraight(List<Card> combinedCards, HandEvaluation highCardHandEvaluation) {
        List<Card> cards = new ArrayList<>(combinedCards);
        Collections.sort(cards);
        // Add aces to the front of the sorted list
        if (cards.getFirst().rank() == Rank.TWO) {
            List<Card> aceCards = new ArrayList<>();
            for (Card card : cards) {
                if (card.rank() == Rank.ACE) {
                    aceCards.add(card);
                }
            }
            for (Card card : aceCards) {
                cards.addFirst(card);
            }
        }

        int l = cards.size() - 2;
        List<Card> consecutiveCards = new ArrayList<>();
        // Iterate backwards through the sorted list to find consecutive cards
        while (l >= 0 && consecutiveCards.size() < 5) {
            int rank = cards.get(l).rank().getValue();
            int rankAbove = cards.get(l + 1).rank().getValue();

            if (rank + 1 == rankAbove) {
                consecutiveCards.add(cards.get(l));
                l--;
                continue;
            }
            if (rank != rankAbove) {
                consecutiveCards.clear();
                consecutiveCards.add(cards.get(l));
            }
            l--;

        }
        if (consecutiveCards.size() == 5) {
            cards.removeAll(consecutiveCards);
            return new HandEvaluation(HandType.STRAIGHT, consecutiveCards, cards);
        }
        return highCardHandEvaluation;
    }

    public static HandEvaluation getHandEvaluation(List<Card> combinedCards) {
        List<Card> cards = new ArrayList<>(combinedCards);
        // Calculate default high-card hand
        HandEvaluation highCardHandEvaluation = new HandEvaluation(HandType.HIGH_CARD, List.of(), cards);

        HandEvaluation bestHandType = highCardHandEvaluation;

        HandEvaluation bestFlush = getBestFlush(new ArrayList<>(cards), highCardHandEvaluation);
        if (bestFlush.getHand() == HandType.ROYAL_FLUSH) {
            return bestFlush;
        }
        if (bestFlush.getHand().getValue() > bestHandType.getHand().getValue()) {
            bestHandType = bestFlush;
        }

        HandEvaluation bestMatching = getBestMatching(new ArrayList<>(cards), highCardHandEvaluation);
        if (bestMatching.getHand().getValue() > bestHandType.getHand().getValue()) {
            bestHandType = bestMatching;
        }

        HandEvaluation bestStraight = getBestStraight(new ArrayList<>(cards), highCardHandEvaluation);
        if (bestStraight.getHand().getValue() > bestHandType.getHand().getValue()) {
            bestHandType = bestStraight;
        }
        return bestHandType;
    }

    private static int compareKickers(List<Card> kickers1, List<Card> kickers2) {
        for (int i = kickers1.size()-1; i >= 0; i--) {
            Card card1 = kickers1.get(i);
            Card card2 = kickers2.get(i);
            int comparison =  card1.compareTo(card2);
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }

    public static List<Integer> getWinningPlayer(Map<Integer, List<Card>> playerHands, List<Card> communityCards) {

        List<Integer> winners = new ArrayList<>();
        HandEvaluation bestHand = null;

        for (Map.Entry<Integer, List<Card>> player : playerHands.entrySet()) {
            List<Card> combinedCards = new ArrayList<>(player.getValue());
            combinedCards.addAll(communityCards);
            HandEvaluation handEvaluation = getHandEvaluation(combinedCards);
            if (bestHand == null) {
                bestHand = handEvaluation;
                winners.add(player.getKey());
                continue;
            }
            if (handEvaluation.getHand().getValue() < bestHand.getHand().getValue()) {
                continue;
            }
            // Hand Type is greater
            if (handEvaluation.getHand().getValue() > bestHand.getHand().getValue()) {
                bestHand = handEvaluation;
                winners.clear();
                winners.add(player.getKey());
                continue;
            }
            // Hand Types are equal tie
            if (!handEvaluation.hasKickers() || handEvaluation.getHand() == HandType.ROYAL_FLUSH) {
                bestHand = handEvaluation;
                winners.add(player.getKey());
                continue;
            }
            // Hand Types are equal kickers to break tie
            int comparison = compareKickers(handEvaluation.getKickers(), bestHand.getKickers());
            if (comparison == 0) {
                bestHand = handEvaluation;
                winners.add(player.getKey());
                continue;
            }
            if (comparison > 0) {
                bestHand = handEvaluation;
                winners.clear();
                winners.add(player.getKey());
            }
        }

        return winners;
    }



}
