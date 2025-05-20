package com.poker.app.game.model.player;

import com.poker.app.game.model.ViewableModel;

public interface Player {

    Move getMove(ViewableModel game);

}
