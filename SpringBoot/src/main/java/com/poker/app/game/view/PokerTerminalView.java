package com.poker.app.game.view;

import com.poker.app.game.model.ViewableModel;
import com.poker.app.game.model.deck.Card;
import com.poker.app.game.model.deck.Rank;
import com.poker.app.game.model.deck.Suit;

import java.util.List;
import java.util.Objects;

public class PokerTerminalView {

    private final ViewableModel model;

    public PokerTerminalView(ViewableModel model) {
        this.model = Objects.requireNonNull(model);
    }


    private String getCardString(Card card) {
        return card.rank().getSymbol() + card.suit().getSymbol() + ((card.rank() != Rank.TEN) ? " " : "");
    }

    public void render() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (model.roundNotStarted()) {
            System.out.println("No round started yet");
            return;
        }
        List<Card> communityCards = model.getCommunityCards();
        String[] cards = new String[5];
        for (int i = 0; i < cards.length; i++) {
            if (i > communityCards.size() - 1) {
                cards[i] = "   ";
                continue;
            }
            cards[i] = getCardString(communityCards.get(i));
        }

        List<Card> playerCards = model.getPlayerCards(model.getTurn());

        int[] chips = model.getPlayerChips();
        int[] bets = model.getPlayerBets();
        List<Integer> activePlayers = model.getActivePlayers();
        String player = "🙎";
        for (int i = 0; i < model.getTurn(); i++) {
            System.out.print("               ");
        }
        System.out.println("⬇");
        for (int i = 0; i < chips.length; i++) {
            // %n to go down
            System.out.printf("%s[$%d] Bet %d | ", player, chips[i], bets[i]);
        }
        System.out.println();
        System.out.printf("""
                ╔════════════════════════════════════════╗
                ║             COMMUNITY CARDS            ║
                ║   ╭────╮ ╭────╮ ╭────╮ ╭────╮ ╭────╮   ║
                ║   │ %s│ │ %s│ │ %s│ │ %s│ │ %s│   ║
                ║   ╰────╯ ╰────╯ ╰────╯ ╰────╯ ╰────╯   ║
                ╚════════════════════════════════════════╝
                Betting Round: %d
                Turn: %d
                Cards: [ %s] [ %s]
                """,
                cards[0], cards[1], cards[2], cards[3], cards[4],
                model.getBettingRound(), model.getTurn(),
                getCardString(playerCards.get(0)), getCardString(playerCards.get(1)), model.getTurn()
                );
    }

}
