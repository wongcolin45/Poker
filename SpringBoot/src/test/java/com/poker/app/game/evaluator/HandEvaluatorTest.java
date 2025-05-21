package com.poker.app.game.evaluator;

import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.deck.CardFactory;
import com.poker.app.game.model.evaluator.HandEvaluator;
import com.poker.app.game.model.evaluator.HandType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



class HandEvaluatorTest {

    private final Map<String, List<Card>> cards;

    public HandEvaluatorTest() {
        cards = new HashMap<>();
    }

    private void testHandEvaluation(List<String> cardStrings, HandType expectedHandType) {
        List<Card> cards = CardFactory.getCards(cardStrings);
        Collections.shuffle(cards);
        HandEvaluator.Evaluation result = HandEvaluator.getHandEvaluation(cards);
        Assertions.assertEquals(expectedHandType, result.getHand());
    }

    @Test
    public void testHandTypes() {
        testHandEvaluation(List.of("A♠", "K♠", "Q♠", "J♠", "10♠", "4♣", "3♦"), HandType.ROYAL_FLUSH);
    }

    @Test
    public void testStraightFlush() {
        testHandEvaluation(List.of("9♥", "8♥", "7♥", "6♥", "5♥", "2♠", "K♦"), HandType.STRAIGHT_FLUSH);
    }

    @Test
    public void testFLush() {
        testHandEvaluation(List.of("A♣", "J♣", "10♣", "6♣", "4♣", "K♠", "3♦"), HandType.FLUSH);
    }

    @Test
    public void testStraight() {
        testHandEvaluation(List.of("9♠", "8♦", "7♣", "6♥", "5♠", "2♣", "K♦"), HandType.STRAIGHT);
    }

    @Test
    public void testFullHouse() {
        testHandEvaluation(List.of("8♠", "8♣", "8♦", "K♠", "K♥", "4♠", "2♠"), HandType.FULL_HOUSE);
    }

    @Test
    public void testFourOfAKind() {
        testHandEvaluation(List.of("Q♠", "Q♥", "Q♦", "Q♣", "9♦", "3♠", "2♥"), HandType.FOUR_OF_A_KIND);
    }

    @Test
    public void test3OfAKind() {
        testHandEvaluation(List.of("6♠", "6♦", "6♣", "Q♠", "9♣", "4♥", "2♦"), HandType.THREE_OF_A_KIND);
    }

    @Test
    public void test2Pair() {
        testHandEvaluation(List.of("J♠", "J♣", "4♦", "4♠", "K♦", "9♥", "2♥"), HandType.TWO_PAIR);
    }

    @Test
    public void test1Pair() {
        testHandEvaluation(List.of("10♠", "10♦", "A♣", "9♣", "6♥", "4♦", "2♣"), HandType.ONE_PAIR);
    }

    @Test
    public void testHighCard() {
        testHandEvaluation(List.of("A♦", "J♠", "9♣", "6♦", "4♠", "3♥", "2♣"), HandType.HIGH_CARD);
    }


}
