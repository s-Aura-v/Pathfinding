package com.aurav.pathfinding;

import com.aurav.pathfinding.screens.StartScreen;

public class Pathfinder extends BaseGame {

    @Override
    public void create() {
        super.create();
        setActiveScreen(new StartScreen());
    }
}
