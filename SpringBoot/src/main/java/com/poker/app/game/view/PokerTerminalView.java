package com.poker.app.game.view;

import com.poker.app.game.model.ViewableModel;
import com.poker.app.game.model.deck.Card;

import java.util.List;
import java.util.Objects;

public class PokerTerminalView {

    private final ViewableModel model;

    private PokerTerminalView(ViewableModel model) {
        this.model = Objects.requireNonNull(model);
    }

    private void render() {
        List<Card> communityCards = model.getCommunityCards();
        String[] cards = new String[5];
        for (int i = 0; i < cards.length; i++) {
            if (i > communityCards.size() - 1) {
                cards[i] = "  ";
            }
            cards[i] = communityCards.get(i).toString();
        }
        System.out.printf("""
                ╔══════════════════════════════════════════════════════════╗
                ║                    COMMUNITY CARDS                       ║
                ║           ╭────╮ ╭────╮ ╭────╮ ╭────╮ ╭────╮             ║
                ║           │ %s │ │ %s │ │ %s │ │ %s │ │ %s │             ║
                ║           ╰────╯ ╰────╯ ╰────╯ ╰────╯ ╰────╯             ║
                ╚══════════════════════════════════════════════════════════╝
                %n""",  cards[0], cards[1], cards[2], cards[3], cards[4]);
    }
}
